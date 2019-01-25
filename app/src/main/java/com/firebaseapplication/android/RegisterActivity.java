package com.firebaseapplication.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signInBtn, signUpBtn;
    private EditText email, password;

    private static final String TAG = "RegisterEmailPassword";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegisterActivity.this);

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.putExtra("email", mAuth.getCurrentUser().getEmail());
            startActivity(intent);
            finish();
        }

        email = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        signInBtn = findViewById(R.id.signin_btn);
        signUpBtn = findViewById(R.id.signup_btn);

        initViews();
    }

    private void initViews() {

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.email_hint), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().toString().equals("")) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.password_hint), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Registering.. Please wait......");
                progressDialog.show();


                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
