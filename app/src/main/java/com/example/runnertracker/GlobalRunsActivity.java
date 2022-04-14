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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GlobalRunsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterGlobal adapterGlobal;
    List<Run> runList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_runs);

        //Initiate recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView = findViewById(R.id.global_recyclerView);
        recyclerView.setLayoutManager(layoutManager);


        //Initiate UserList
        runList = new ArrayList<>();

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
                        Intent intent1 = new Intent(GlobalRunsActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_record:
                        Intent intent2 = new Intent(GlobalRunsActivity.this, RecordJourney.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_view:
                        Intent intent3 = new Intent(GlobalRunsActivity.this, ViewJourneys.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_stats:
                        Intent intent4 = new Intent(GlobalRunsActivity.this, StatisticsActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.ic_profile:
                        Intent intent5 = new Intent(GlobalRunsActivity.this, EditProfileActivity.class);
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Runs");

        //get all data from path
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                runList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Run run = ds.getValue(Run.class);

                    runList.add(run);

                    adapterGlobal = new AdapterGlobal(getApplicationContext(), runList);
                    recyclerView.setAdapter(adapterGlobal);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}