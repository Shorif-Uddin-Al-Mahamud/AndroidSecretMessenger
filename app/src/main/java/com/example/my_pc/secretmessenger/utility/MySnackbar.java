package com.example.my_pc.secretmessenger.utility;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class MySnackbar {


    private Activity activity;

    public MySnackbar(Activity activity) {

        this.activity = activity;
    }

    public void viewSnackBar(String message) {

        View view1 = activity.findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(view1, message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.RED);

        View view2 = snackbar.getView();
        TextView tv = view2.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.YELLOW);

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ---- Just hide,,,when user clicked ok
            }
        });

        snackbar.show();

    }
}
