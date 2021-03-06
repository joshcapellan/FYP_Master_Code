package com.example.runnertracker;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;


import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder>{

    Context context;
    List<User> userList;

    public AdapterUsers(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_user, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //get data
        String userName = userList.get(i).getFullname();
        double userScore = userList.get(i).getTotalKm();
        String userEmail = userList.get(i).getEmail();
        String userAge = userList.get(i).getAge();

        String stringUserScore = Double.toString(userScore);

        //set data
        myHolder.mNameTv.setText(userName);
        myHolder.mScoreTv.setText("Total Kms " + stringUserScore + " km");
        myHolder.mEmailTv.setText("Email: " + userEmail);
        myHolder.mAgeTv.setText("Age " + userAge);

        //handle item click
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ""+ userEmail, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {

        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{


        TextView mNameTv, mEmailTv, mAgeTv, mScoreTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mNameTv = itemView.findViewById(R.id.rowName);
            mScoreTv = itemView.findViewById(R.id.rowScore);
            mEmailTv = itemView.findViewById(R.id.rowEmail);
            mAgeTv = itemView.findViewById(R.id.rowAge);

        }
    }

}