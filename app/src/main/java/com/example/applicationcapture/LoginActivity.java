package com.example.applicationcapture;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private ProgressBar progressBar;
    private EditText email, psw;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        email = (EditText) findViewById(R.id.email);
        psw = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    public void signin(View view) {
        String uemail = email.getText().toString();
        String Psw = psw.getText().toString();
        if (validate()){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(uemail,Psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.GONE);
                        EmailVerfication();
//                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }
                }
            });
        }else {
            Toast.makeText(this, "Login Failed....", Toast.LENGTH_SHORT).show();
        }
    }

    public void signup(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void forget(View view) {
        startActivity(new Intent(this, ForgetPswActivity.class));
    }



    private boolean validate() {
        boolean valid = true;
        String uemail = email.getText().toString();
        String Psw = psw.getText().toString();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (uemail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
                email.setError("enter a valid email address");
                valid = false;
            } else {
                email.setError(null);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Psw.isEmpty() || Psw.length() < 4 || Psw.length() > 10) {
                psw.setError("between 4 and 10 alphanumeric characters");
                valid = false;
            } else {
                psw.setError(null);
            }
        }



        return valid;
    }

    public void EmailVerfication(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        boolean verify = firebaseUser.isEmailVerified();

        if (verify){
//            Toast.makeText(LoginActivity.this, "Email has been Verified..!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }else{
            Toast.makeText(LoginActivity.this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            finish();

        }
    }
}

