package com.example.my_pc.secretmessenger.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeUserNameActivity extends BaseActivity {

    @BindView(R.id.currentUserName)
    TextView currentUserName;
    @BindView(R.id.newUserName)
    EditText newUserName;
    @BindView(R.id.changeUserNameBtn)
    Button changeUserNameBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initVariable();
    }

    private void initView() {
        setContentView(R.layout.activity_change_user_name);
        ButterKnife.bind(this);

        setTitle("Update User Name");
        currentUserName.setText(User.USER_NAME);

        progressDialog = getProgressDialog("Updating...", "Please wait.");
    }

    private void initVariable() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.changeUserNameBtn)
    public void onViewClicked() {

        if (check()) {
            return;
        }

        /*new AlertDialog.Builder(this).setTitle("Change User Name").setMessage("Are you sure to change the user name ?")
                .setIcon(R.drawable.ic_warning).setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.show();
                        changeUserName();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing here
            }
        }).create().show();
        */

        // Show alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false);
        builder.setTitle("Change User Name?").setMessage("Are you sure to change the user name ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                progressDialog.show();
                changeUserName();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing here
            }
        });

        AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#BCAAA4")));

        dialog.show();

    }

    private void changeUserName() {

        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUserName.getText().toString().trim()).build();

        if (firebaseUser != null) {
            firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        viewSnackBar("User Name Change Successfull");
                        newUserName.setText("");

                        MainActivity.mainActivity.initProfile();
                        currentUserName.setText(User.USER_NAME);
                    } else {
                        viewSnackBar("Something went wrong. Please try again.");
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            });
        }


    }

    private boolean check() {

        if (newUserName.getText().toString().trim().isEmpty()) {
            newUserName.setError("User Name is required");
            newUserName.requestFocus();
            return true;
        }
        return false;
    }
}
