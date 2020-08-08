package com.example.applicationcapture;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {
    private EditText Email, fullname, password, c_password;
    private FirebaseAuth mAuth;
    private static final String TAG = "RegistrationActivity";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseStorage firebaseStorage;
    private DatabaseReference mRef ;
    private ProgressBar progressBar;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email = (EditText) findViewById(R.id.et_remail);
        fullname = (EditText) findViewById(R.id.et_rname);
        password = (EditText) findViewById(R.id.et_rpsw);
        c_password = (EditText) findViewById(R.id.et_rcpsw);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        mRef = db.getReference("Users");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){

        }

    }

    public void register(View view) {
        if (validate()){

            final String uemail = Email.getText().toString().trim();
            final String psw = password.getText().toString().trim();
            final String fname = fullname.getText().toString();
            progressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(uemail, psw)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                                String userId = mDatabase.push().getKey();
                                User user_data = new User (uemail,fname,psw);
                                mDatabase.child(mAuth.getUid()).child("user data").setValue(user_data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            EmailVerification();
                                        }
                                    }
                                });
                            }
                        } });


        }
        else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this,"Enter Proper Details",Toast.LENGTH_LONG).show();
        }
    }


    private boolean validate() {
        boolean valid = true;
        String uemail = Email.getText().toString();
        String fname = fullname.getText().toString();
        String psw = password.getText().toString();
        String c_psw = c_password.getText().toString();


        if (fname.isEmpty() || fname.length() < 8 || fname.length() > 50) {
            fullname.setError("at least 8 characters");
            valid = false;
        } else {
            fullname.setError(null);
        }


        if (uemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
            Email.setError("enter a valid email address");
            valid = false;
        } else {
            Email.setError(null);
        }

        if (psw.isEmpty() || psw.length() < 4 || psw.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (c_psw.isEmpty() || c_psw.length() < 4 || c_psw.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        if (c_psw.equals(psw)) {
            password.setError(null);
        } else {
            password.setError("Your Password is not Same");
            valid = false;
        }

        return valid;
    }

    public void EmailVerification(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Successfully Register...!",Toast.LENGTH_SHORT);
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    }else {
                        Toast.makeText(RegisterActivity.this,"Mail has been send to Your Email Id",Toast.LENGTH_SHORT);

                    }
                }
            });
        }
    }

    public void signin(View view) {
    }
}

