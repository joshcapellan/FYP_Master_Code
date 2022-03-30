package com.example.runnertracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //initialisation of components
    private FirebaseAuth auth;
    private EditText emailEt;
    private EditText passwordEt;
    private Button signupBtn;
    private Button loginBtn;
    TextView resetPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailEt = findViewById(R.id.email_edt_text);
        passwordEt = findViewById(R.id.pass_edt_text);

        signupBtn = findViewById(R.id.signup_btn);
        loginBtn = findViewById(R.id.login_btn);

        resetPasswordTv = findViewById(R.id.reset_pass_tv);

        auth = FirebaseAuth.getInstance();

        signupBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        resetPasswordTv.setOnClickListener(this);

    }


    //switch method used to deal with the different buttons within the login page
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //redirects user to the sign up page
            case R.id.signup_btn:
                Intent intentSignup = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intentSignup);
                break;

           //redirects user to the forgot password actvity
            case R.id.reset_pass_tv:
                Intent intentReset = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intentReset);
                break;

            //starts the log in progress
            case R.id.login_btn:

                //contents of both email and password edit text converted into string variables
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();


                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    //firebase function that allows users to log in with their email and password
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_LONG).show();
                                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intentMain);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Log in Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }

                break;


        }
    }
}