package com.example.playersquiz.ui.game;


import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.playersquiz.R;

class ALoading{

    Activity activity;
    AlertDialog dialog;

    ALoading(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
