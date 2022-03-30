package com.example.runnertracker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Settings {

    public Context context;
    Settings(Context context){
        this.context=context;
    }

    public void github(){
        Intent github = new Intent(Intent.ACTION_VIEW);
        github.setData(Uri.parse("https://github.com/joshcapellan"));
        context.startActivity(github);
    }

    public void tud(){
        Intent tud = new Intent(Intent.ACTION_VIEW);
        tud.setData(Uri.parse("https://www.tudublin.ie/explore/our-campuses/blanchardstown/"));
        context.startActivity(tud);
    }

    public void share(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "Runner Tracker App");
        share.putExtra(Intent.EXTRA_TEXT, "Hope you enjoyed the app!");
        context.startActivity(Intent.createChooser(share, "Choose method"));
    }

}
