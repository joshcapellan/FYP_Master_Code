package com.example.runnertracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    //initialisation of components
    private FirebaseAuth auth;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText fullnameEt;
    private EditText ageEt;
    private Button signUpBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.email_edt_text);
        passwordEt = findViewById(R.id.pass_edt_text);
        fullnameEt = findViewById(R.id.fullname_edt_text);
        ageEt = findViewById(R.id.age_edt_text);

        loginBtn = findViewById(R.id.login_btn);
        signUpBtn = findViewById(R.id.signup_btn);

        signUpBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }




    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login_btn:
                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.signup_btn:
                registerUser();
                break;
        }





//    //switch method used to deal with the button options provided within the sign up page
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()){
//            //button that directs users back to the login page
//            case R.id.login_btn:
//                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intentLogin);
//                break;
//
//            //button that starts sign up process
//            case R.id.signup_btn:
//
//                //contents of both email and password edit text converted into string variables
//                String email = emailEt.getText().toString();
//                String password = passwordEt.getText().toString();
//
//                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))  {
//                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
//                } else{
//                    //firebase method that allows users to sign up an account with email and password
//                    auth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(this, task -> {
//                            if(task.isSuccessful()){
//                                Toast.makeText(getApplicationContext(),"Successfully Registered", Toast.LENGTH_LONG).show();
//                                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intentMain);
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                }
//
//                break;
//        }
    }

    private void registerUser() {
    String email = emailEt.getText().toString().trim();
    String password = passwordEt.getText().toString().trim();
    String fullname = fullnameEt.getText().toString().trim();
    String age = ageEt.getText().toString().trim();

    if(fullname.isEmpty()){
        fullnameEt.setError("Please enter full name.");
        fullnameEt.requestFocus();
        return;
    }

    if(age.isEmpty()){
        ageEt.setError("Please enter age.");
        ageEt.requestFocus();
        return;
    }

    if(password.isEmpty()){
        passwordEt.setError("Please enter password.");
        passwordEt.requestFocus();
        return;
    }

    if(password.length() < 6){
        passwordEt.setError("Password must be more than 6 characters");
        passwordEt.requestFocus();
        return;
    }

    if(email.isEmpty()){
        emailEt.setError("Please enter email.");
        emailEt.requestFocus();
        return;
    }

    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        emailEt.setError(("Please provide valid email."));
        emailEt.requestFocus();
        return;
        }

    auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        User user = new User(fullname, age, email, password);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Successfully Registered", Toast.LENGTH_LONG).show();
                                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intentMain);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

}