package com.example.applicationcapture;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference Db_Ref;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        Db_Ref = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userKey = user.getUid();

        Db_Ref.child("Users").child(userKey).child("user data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userID = dataSnapshot.child("uemail").getValue(String.class);
                Toast.makeText(HomeActivity.this, "email : " + userID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void logout(View view) {
        mAuth.signOut();
        startActivity(new Intent(this,LoginActivity.class));

    }

    public void open_cam(View view) {
        if (userID!=null) {
            Intent intent = new Intent(this, Camera_Activity.class);
            intent.putExtra("EMAIL",userID);
            startActivity(intent);
        }
    }

    public void theft(View view) {
        startActivity(new Intent(this,Theft_Activity.class));

    }
}
