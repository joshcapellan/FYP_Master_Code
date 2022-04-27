package com.example.runnertracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private ImageView profilePic;
    int TAKE_IMAGE_CODE = 10001;

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
                    String password = userProfile.password;
                    double totalKm = userProfile.totalKm;

                    fullnameEt.setText(fullName);
                    emailEt.setText(email);
                    ageEt.setText(age);
                    passwordEt.setText(password);

                    if(user.getPhotoUrl() != null){
                        Glide.with(EditProfileActivity.this)
                                .load(user.getPhotoUrl())
                                .into(profilePic);
                    }


                    updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String newFullName = fullnameEt.getText().toString();
                            String newAge = ageEt.getText().toString();
                            String newEmail = emailEt.getText().toString();
                            String newPass = passwordEt.getText().toString();
                            double newTotalKm = totalKm;

                            update(newFullName, newAge, newEmail, newPass, newTotalKm);

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

    private void update(String fullname, String age, String email, String password, double totalKm){

        DatabaseReference DbRef = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        User user = new User(fullname, age, email, password, totalKm);
        DbRef.setValue(user);
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.back_btn3:
                Intent intentProfile = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentProfile);
                break;

        }
    }

    public void handleImageClick(View view) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TAKE_IMAGE_CODE){
            switch(resultCode){
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profilePic.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }
    }

    private void handleUpload(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference referenceImg = FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uid + ".jpeg");

        referenceImg.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(referenceImg);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: ", e.getCause());
                    }
                });

    }

    private void getDownloadUrl(StorageReference referenceImg){
        referenceImg.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess" + uri);
                        setUserProfileUrl(uri);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Profile Image failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}