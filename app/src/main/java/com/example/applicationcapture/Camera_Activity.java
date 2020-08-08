package com.example.applicationcapture;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Vibrator;
import android.se.omapi.Session;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicationcapture.motiondetection.Comparer;
import com.example.applicationcapture.motiondetection.ImageProcessing;
import com.example.applicationcapture.motiondetection.MotionDetector;
import com.example.applicationcapture.motiondetection.MotionDetectorCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Random;
import com.example.applicationcapture.GmailSender;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Camera_Activity extends AppCompatActivity {
    Session session = null;
    Context context = null;
    private TextView txtStatus;
    private MotionDetector motionDetector;
    ImageView imageCaptured;
    private String userID;
    private int cnt = 0;
    private DatabaseReference Db_Ref;
    String myEmailString = "applicationcapture@gmail.com";
    String passString = "capturejdk123";
    String subjectString = "Virtual Camera Security got Theft";
    String textString;
    byte[] data;
    FileOutputStream out;
    Uri downloadUrl;
    String emailva;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_);
        Intent intent = getIntent();
        emailva = intent.getStringExtra("EMAIL");
        context = this;

        if (ActivityCompat.checkSelfPermission(Camera_Activity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Camera_Activity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4789);
        }

//          pictureCallback = new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] bytes, Camera camera) {
//                Toast.makeText(MainActivity.this, "Photo clicked", Toast.LENGTH_SHORT).show();
//                    MotionDetector.releaseCamera();
//            }
//        };
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        txtStatus = (TextView) findViewById(R.id.txtStatus);
        imageCaptured = findViewById(R.id.imageCaptured);
        motionDetector = new MotionDetector(this, (SurfaceView) findViewById(R.id.surfaceView));


        Db_Ref = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userKey = user.getUid();

        Db_Ref.child("Users").child(userKey).child("user data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userID = dataSnapshot.child("uemail").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


            motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
                @Override
                public void onMotionDetected() {


                    byte[] c_image = new byte[MotionDetector.changedImageArray.length];


                    for (int x = 0; x < MotionDetector.changedImageArray.length; x++) {
                        c_image[x] = (byte) MotionDetector.changedImageArray[x];
                    }
//                Bitmap bitmap= BitmapFactory.decodeByteArray(c_image,0,c_image.length);
                    //  Bitmap bmp= ImageProcessing.rgbToBitmap(MotionDetector.changedImageArray,MotionDetector.img_width,MotionDetector.img_height);


                    Bitmap bitmap = Bitmap.createBitmap(MotionDetector.changedImageArray,
                            MotionDetector.img_width, MotionDetector.img_height, Bitmap.Config.RGB_565); //ARGB_8888);

//                    int[] rgb = ImageProcessing.decodeYUV420SPtoRGB(c_image, MotionDetector.img_width,  MotionDetector.img_height);



                    imageCaptured.setImageBitmap(bitmap);
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(20);

                    txtStatus.setText("Theft detected " + (++cnt));
                    Toast.makeText(Camera_Activity.this, "step 1 start", Toast.LENGTH_SHORT).show();

                    /////////////////////////////////
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/TheftDetector");
                    myDir.mkdirs();
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();
                    try {
                        out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Camera_Activity.this, "step 1 done", Toast.LENGTH_SHORT).show();
                    /* //////////////////////////////// */
                    Db_Ref = FirebaseDatabase.getInstance().getReference();
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    String userKey = user.getUid();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    data = baos.toByteArray();

                    Toast.makeText(Camera_Activity.this, "step 2 start", Toast.LENGTH_SHORT).show();


                    StorageReference ref = storageReference.child("Theft").child(emailva).child(String.valueOf(System.currentTimeMillis()));
                    ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            downloadUrl = uriTask.getResult();


                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                            theft_data user_data = new theft_data(downloadUrl.toString());
                            mDatabase.child(FirebaseAuth.getInstance().getUid()).child("user data").child("Theft").child(String.valueOf(System.currentTimeMillis())).setValue(user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Camera_Activity.this, "ss", Toast.LENGTH_SHORT).show();

                                    //////////////////////////
//                                Properties props = new Properties();
//                                props.put("mail.smtp.host","smtp.gmail.com");
//                                props.put("mail.smtp.socketFactory.port","465");
//                                props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//                                props.put("mail.smtp.auth","true");
//                                props.put("mail.smtp.port","465");
//
//                                session = Session.getDefaultInstance(props)

                                    String email = emailva;
                                    String subject = "Virtual Security Cam";
                                    String message = "Hello" +
                                            "Virtual Security Cam got one suspect click here : "+" "
                                            + downloadUrl.toString();

                                    SendMail sm = new SendMail(context, email, subject, message);

                                    //Executing sendmail to send email
                                    sm.execute();

                                    //////////////////////

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Camera_Activity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(Camera_Activity.this, "step 2 done", Toast.LENGTH_SHORT).show();


                    /////////////////////////////////////


                }

                @Override
                public void onTooDark() {
                    txtStatus.setText("Too dark here");
                }
            });

            ////// Config Options
            //motionDetector.setCheckInterval(500);
            //motionDetector.setLeniency(20);
            //motionDetector.setMinLuma(1000);

    }


    @Override
    protected void onResume() {
        super.onResume();
        motionDetector.onResume();

        if (motionDetector.checkCameraHardware()) {
            txtStatus.setText("Camera found");
        } else {
            txtStatus.setText("No camera available");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionDetector.onPause();
    }

    public void mail(View view) {
        //Getting content for email
        String email = emailva;
        String subject = "Virtual Security Cam";
        String message = "Hello" +
                "Virtual Security Cam got one suspect";
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }




}