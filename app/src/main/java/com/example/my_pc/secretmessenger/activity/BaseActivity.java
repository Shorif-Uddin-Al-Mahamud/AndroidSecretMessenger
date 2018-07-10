package com.example.my_pc.secretmessenger.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.aes.AES;

public class BaseActivity extends AppCompatActivity {

    public static boolean isSecret = false;
    public static String secretPass = "";
    public static String decriptMessage = "";
    public static  String origianlMessage = "";


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

}
