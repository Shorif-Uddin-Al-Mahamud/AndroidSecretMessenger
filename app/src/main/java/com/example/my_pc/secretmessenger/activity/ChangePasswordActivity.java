package com.example.my_pc.secretmessenger.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.input_current_password)
    EditText inputCurrentPassword;
    @BindView(R.id.input_new_password)
    EditText inputNewPassword;
    @BindView(R.id.input_confirm_password)
    EditText inputConfirmPassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initVariable();
    }

    private void initView() {
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        setTitle("Change Password");
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

    public void changePasswordButton(View view) {

        // Check the input field
        if (!check()) {
            return;
        }

        checkCurrentPassword();

    }

    private boolean check() {
        if (inputCurrentPassword.getText().toString().trim().isEmpty()) {
            inputCurrentPassword.setError("Enter Current Password");
            inputCurrentPassword.requestFocus();
            return false;
        }

        if (inputNewPassword.getText().toString().trim().isEmpty()) {

            inputNewPassword.setError("Enter New Password");
            inputNewPassword.requestFocus();
            return false;
        }


        if (inputConfirmPassword.getText().toString().trim().isEmpty()) {
            inputConfirmPassword.setError("Enter Confirm Password");
            inputConfirmPassword.requestFocus();
            return false;
        }

        if (!inputNewPassword.getText().toString().trim().equals(inputConfirmPassword.getText().toString().trim())) {
            inputConfirmPassword.setError("Confirm Password Does't Matches");
            inputConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void checkCurrentPassword() {
        firebaseAuth.signInWithEmailAndPassword(User.USER_EMAIL, inputCurrentPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    changePassword();
                } else {
                    viewSnackBar("Current password is wrong");
                }
            }
        });
    }

    private void changePassword() {
        firebaseUser.updatePassword(inputNewPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    viewSnackBar("Your password is changed successfully.");
                } else {
                    viewSnackBar("Failed. Please try again later.");
                }
            }
        });
    }

    public void cancelPasswordChange(View view) {
        inputConfirmPassword.setText("");
        inputCurrentPassword.setText("");
        inputNewPassword.setText("");
    }
}
