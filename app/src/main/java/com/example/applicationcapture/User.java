package com.example.applicationcapture;

public class User {

    public String uemail,uname,upsw;

    public User() {
    }

    public User(String uemail, String uname, String upsw) {
        this.uemail = uemail;
        this.uname = uname;
        this.upsw = upsw;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpsw() {
        return upsw;
    }

    public void setUpsw(String upsw) {
        this.upsw = upsw;
    }
}

//
//    public void sendEmail()
//    {
//        try
//        {
//            email = et_email.getText().toString();
//            subject = et_subject.getText().toString();
//            message = et_message.getText().toString();
//            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//            emailIntent.setType("plain/text");
//            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { email });
//            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,subject);
//            if (URI != null) {
//                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
//            }
//            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
//            this.startActivity(Intent.createChooser(emailIntent,"Sending email..."));
//        }
//        catch (Throwable t)
//        {
//            Toast.makeText(this, "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
//        }

//database = FirebaseDatabase.getInstance();
//        Db_Ref = FirebaseDatabase.getInstance().getReference();
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        String userKey = user.getUid();
//
//        Db_Ref.child("Users").child(userKey).child("user data").addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        String userID = dataSnapshot.child("uemail").getValue(String.class);
//        Toast.makeText(Camera_Activity.this, "email : "+userID, Toast.LENGTH_SHORT).show();
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//        });
