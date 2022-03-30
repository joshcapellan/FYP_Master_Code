package com.example.runnertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


/*
TO DO
    1. Allow user to record a journey which is saved to a database. The path, time, distance, date via gps. (DONE)
    2. Allow user to stop, start a journey. Stopping a journey causes it to be saved. (DONE)
    3. Allow user to attach image to a saved journey (DONE)
    4. Allow user to rate a journey out of 5 (DONE)
    5. Allow user to add comments about a certain journey (DONE)
    6. Allow user to see a list of recorded journeys filtered by date (DONE)
    7. Clicking on a recorded journey displays more information (rating, comments, picture, time, distance, average speed, path on google maps) (DONE)
    8. Allow user to see statistics page which shows how far ran today, this week, this month, all time and could graph these (DONE)
    9. Allow user to set a goal for km to run every week, display whether the goal has been reached or not in the app. (DONE)
    10. Ask user for GPS permissions (DONE)
    11. broadcast receiver so that when battery is low do fewer GPS requests (DONE)
    12. Display notification while tracking a journey (DONE)

    Need
        - Service (for GPS tracking)
        - Activities (to display stats, journeys, single journeys, recording a journey, home page)
        - Database (to store journey information)
        - Content Provider (in order to access the database)
        - Broadcast Receiver to register callbacks and reduce GPS request frequency on low battery
 */
public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    private static final int PERMISSION_GPS_CODE = 1;
    private static final int PERMISSION_COAL_GPS_CODE = 2;
    FirebaseAuth auth;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private TextView greeting, infoName, infoAge, infoEmail;
    Settings settings = new Settings(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        greeting = (TextView)findViewById(R.id.greetingTextView);
        infoName = (TextView)findViewById(R.id.infoName);
        infoAge = (TextView)findViewById(R.id.infoAge);
        infoEmail = (TextView)findViewById(R.id.infoEmail);


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullname;
                    String email = userProfile.email;
                    String age = userProfile.age;

                    greeting.setText("Welcome " + fullName);
                    infoName.setText(fullName);
                    infoAge.setText(age);
                    infoEmail.setText(email);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error try again", Toast.LENGTH_LONG).show();
            }
        });






        ///////////NAV BAR CODE//////////////////////////////////
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:

                        break;

                    case R.id.ic_record:
                        Intent intent1 = new Intent(MainActivity.this, RecordJourney.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_view:
                        Intent intent2 = new Intent(MainActivity.this, ViewJourneys.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_stats:
                        Intent intent3 = new Intent(MainActivity.this, StatisticsActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_profile:
                        Intent intent4 = new Intent(MainActivity.this, EditProfileActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        //////////////////////////////////////////////////////////////////

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_github) {
            settings.github();
        }

        else if (id == R.id.tud) {
            settings.tud();
        }

        else if (id == R.id.share) {
            settings.share();
        }

        if(id == R.id.action_search){
        Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
        return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////







    /////////////////////////////////////////////////////////////////////////////////////////////
    public void onClickRecord(View v) {
        // go to the record journey activity
        Intent journey = new Intent(MainActivity.this, RecordJourney.class);
        startActivity(journey);
    }

    public void onClickView(View v) {
        // go to the activity for displaying journeys
        Intent view = new Intent(MainActivity.this, ViewJourneys.class);
        startActivity(view);
    }

    public void onClickStatistics(View v) {
        // go to the activity for displaying statistics
        Intent stats = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(stats);
    }

    public void onClickLogout(View v) {
        // go to the activity for displaying statistics
        Intent logout = new Intent(MainActivity.this, LoginActivity.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(logout);
    }

    public void onClickProfile(View v) {
        // go to the activity for displaying profile
        Intent profile = new Intent(MainActivity.this, EditProfileActivity.class);
        startActivity(profile);
    }

}
