package com.example.my_pc.secretmessenger.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.aes.AES;

public class BaseActivity extends AppCompatActivity {

    public static boolean isSecret = false;
    public static String secretPass = "";
    public static String decriptMessage = "";
    public static String origianlMessage = "";


    public void showAlertDialogForGetKey(String msg) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.get_key_alert_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edittext_key);

        // dialogBuilder.setTitle("Enter Key");
        dialogBuilder.setMessage("Enter Key");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                secretPass = edt.getText().toString().trim();
                decriptMessage = AES.decrypt(origianlMessage, secretPass);
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }


    public void viewSnackBar(String message) {

        View view1 = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view1, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);

        View view2 = snackbar.getView();
        TextView tv = view2.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ---- Just hide,,,when user clicked ok
            }
        });

        snackbar.show();

    }


    public boolean isInternetConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }



    private ProgressDialog progressDialog;


    public ProgressDialog getProgressDialog(String title, String message) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.drawable.ic_info_icone);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);

        return progressDialog;

    }


}
