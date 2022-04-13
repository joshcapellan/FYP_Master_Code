package com.example.runnertracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    List<User> userList;
    private Query reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        //Initiate recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView = findViewById(R.id.users_recyclerView);
        recyclerView.setLayoutManager(layoutManager);


        //Initiate UserList
        userList = new ArrayList<>();

        getAllUsers();

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
                        Intent intent1 = new Intent(LeaderboardActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_record:
                        Intent intent2 = new Intent(LeaderboardActivity.this, RecordJourney.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_view:
                        Intent intent3 = new Intent(LeaderboardActivity.this, ViewJourneys.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_stats:
                        Intent intent4 = new Intent(LeaderboardActivity.this, StatisticsActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.ic_profile:
                        Intent intent5 = new Intent(LeaderboardActivity.this, EditProfileActivity.class);
                        startActivity(intent5);
                        break;
                }


                return false;
            }
        });

        //////////////////////////////////////////////////////////////////
    }

    private void getAllUsers() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users").orderByChild("totalKm");

        //get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    User user = ds.getValue(User.class);


                    userList.add(user);

                    adapterUsers = new AdapterUsers(getApplicationContext(), userList);
                    recyclerView.setAdapter(adapterUsers);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}