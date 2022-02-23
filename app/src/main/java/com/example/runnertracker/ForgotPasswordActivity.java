package com.example.runnertracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    //initialisation of components
    private FirebaseAuth auth;
    private EditText emailEt;
    private Button resetPasswordBtn;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.email_edt_text);

        resetPasswordBtn = findViewById(R.id.reset_pass_btn);
        back = findViewById(R.id.back_btn);

        resetPasswordBtn.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    //switch method used to deal with the button options provided within the password page
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            //redirects user back to the login page
            case R.id.back_btn:
                Intent intentBack = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentBack);
                break;

            //starts the if statements to deal with password reset
            case R.id.reset_pass_btn:

                //contents of email edit text converted into a string variable
                String email = emailEt.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
                } else{
                    //firebase function that takes the users email and ends a reset email
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(this, task -> {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Please check your email for password reset.", Toast.LENGTH_LONG).show();
//                                    Intent intentMain = new Intent(getApplicationContext(), LoginActivity.class);
//                                    startActivity(intentMain);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Password reset failed", Toast.LENGTH_LONG).show();
                                }
                            });
                }

                break;
        }


    }
}