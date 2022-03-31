package com.example.runnertracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    //initialisation of components

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private FirebaseAuth auth;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText fullnameEt;
    private EditText ageEt;
    private Button updateBtn;
    private Button backBtn;

    private ImageView profilePic;
    public Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();

        emailEt = findViewById(R.id.email_edt_text);
        passwordEt = findViewById(R.id.pass_edt_text);
        fullnameEt = findViewById(R.id.fullname_edt_text);
        ageEt = findViewById(R.id.age_edt_text);

        updateBtn = findViewById(R.id.update_btn);
        backBtn = findViewById(R.id.back_btn3);
        profilePic = findViewById(R.id.profilePicture1);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        updateBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullname;
                    String email = userProfile.email;
                    String age = userProfile.age;

                    fullnameEt.setText(fullName);
                    emailEt.setText(email);
                    ageEt.setText(age);

                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String newFullName = fullnameEt.getText().toString();
                            String newAge = ageEt.getText().toString();
                            String newEmail = emailEt.getText().toString();
                            String newPass = passwordEt.getText().toString();

                            update(newFullName, newAge, newEmail, newPass);

                            user.updateEmail(emailEt.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"Email updated", Toast.LENGTH_SHORT).show();
                                }
                            });

                            user.updatePassword(passwordEt.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(),"Password updated", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error try again", Toast.LENGTH_LONG).show();
            }
        });




        ///////////NAV BAR CODE//////////////////////////////////
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent(EditProfileActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_record:
                        Intent intent2 = new Intent(EditProfileActivity.this, RecordJourney.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_view:
                        Intent intent3 = new Intent(EditProfileActivity.this, ViewJourneys.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_stats:
                        Intent intent4 = new Intent(EditProfileActivity.this, StatisticsActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.ic_profile:
                        break;
                }


                return false;
            }
        });

        //////////////////////////////////////////////////////////////////







    }

    private void update(String fullname, String age, String email, String password){

        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        User user = new User(fullname, age, email, password);
        DbRef.setValue(user);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profilePic.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image");
        pd.show();


        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/"+randomKey);

        riversRef.putFile(imageUri)

            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_SHORT).show();
                }
            })

            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Snackbar.make(findViewById(R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                }
            })

            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                }
            });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back_btn3:
                Intent intentProfile = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentProfile);
                break;

    }
}}