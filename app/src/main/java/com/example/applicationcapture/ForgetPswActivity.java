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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPswActivity extends AppCompatActivity {
    private EditText fp_email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);

        fp_email = (EditText)findViewById(R.id.femail);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
    }

    public void forget(View view) {
        if (validate()){
            progressBar.setVisibility(View.VISIBLE);
            final String email = fp_email.getText().toString().trim();
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ForgetPswActivity.this,"Mail has been send to your Email Account....!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ForgetPswActivity.this,LoginActivity.class));
                    }else{
                        Toast.makeText(ForgetPswActivity.this,"Password Reset Proccess fail.....!",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }else{
            Toast.makeText(ForgetPswActivity.this,"Proccess Fail.....!",Toast.LENGTH_SHORT).show();

        }
    }

    public void login(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    private boolean validate() {
        boolean valid = true;
        String email =fp_email.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            fp_email.setError("enter a valid email address");
            valid = false;
        } else {
            fp_email.setError(null);
        }

        return valid;
    }
}
