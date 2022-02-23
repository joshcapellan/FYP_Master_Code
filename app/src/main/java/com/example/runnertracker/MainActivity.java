package com.example.runnertracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;


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
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_GPS_CODE = 1;
    private static final int PERMISSION_COAL_GPS_CODE = 2;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

    }


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
        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(profile);
    }

}
