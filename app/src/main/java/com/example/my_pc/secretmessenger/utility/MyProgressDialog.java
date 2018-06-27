package com.example.my_pc.secretmessenger.utility;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.my_pc.secretmessenger.R;

public class MyProgressDialog {

    private Context context;
    private ProgressDialog progressDialog;


    public MyProgressDialog(Context context) {
        this.context = context;

    }

    public ProgressDialog create(String title, String message) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setIcon(R.drawable.ic_info_icone);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);

        return progressDialog;

    }


    public ProgressDialog create() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setIcon(R.drawable.ic_info_icone);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please wait");

        return progressDialog;

    }


}
