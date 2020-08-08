package com.example.applicationcapture;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Theft_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<theft_data> list;
    private Theft_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String userKey = user.getUid();
        Toast.makeText(this, ""+userKey, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView)findViewById(R.id.rv_theft);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<theft_data>();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userKey)
                .child("user data").child("Theft");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    theft_data psclData = dataSnapshot1.getValue(theft_data.class);
                    list.add(psclData);
                }
                adapter = new Theft_Adapter(Theft_Activity.this,list);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Theft_Activity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
