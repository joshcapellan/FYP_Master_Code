package com.example.runnertracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterGlobal extends RecyclerView.Adapter<AdapterGlobal.MyHolder>{

    Context context;
    List<Run> globalList;

    public AdapterGlobal(Context context, List<Run> globalList){
        this.context = context;
        this.globalList = globalList;

    }

    @NonNull
    @Override
    public AdapterGlobal.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_global, viewGroup, false);

        return new AdapterGlobal.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGlobal.MyHolder myHolder, int i) {
        //get data
        String userName = globalList.get(i).getFullName();
        String userDistance = globalList.get(i).getDistance();
        String userTime = globalList.get(i).getDuration();

        //set data
        myHolder.mNameTv.setText(userName);
        myHolder.mDistanceTv.setText(userDistance);
        myHolder.myDurationTv.setText(userTime);

    }

    @Override
    public int getItemCount() {

        return globalList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{


        TextView mNameTv, mDistanceTv, myDurationTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            mNameTv = itemView.findViewById(R.id.rowGlName);
            mDistanceTv = itemView.findViewById(R.id.rowGlDistance);
            myDurationTv = itemView.findViewById(R.id.rowGlTime);

        }
    }

}
