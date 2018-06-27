package com.example.my_pc.secretmessenger.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.example.my_pc.secretmessenger.utility.MyProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LogInFragment extends Fragment {


    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.link_signup)
    TextView linkSignup;
    Unbinder unbinder;
    private View view;

    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_in, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Log In");


        initVariable();

    }

    private void initVariable() {

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new MyProgressDialog(getActivity()).create("Signing in...", "Please wait");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClicked() {

        if (!checkValidation()) {
            return;
        }


        signIn();
    }

    @OnClick(R.id.link_signup)
    public void onLinkSignupClicked() {


        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new SignUpFragment()).addToBackStack("tag").commit();

    }


    private void signIn() {


        progressDialog.show();

        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();

            User.USER_EMAIL = "";
            User.USER_NAME = "";
            User.USER_PHOTO = "";
        }

        /*
         * User Sign In
         */
        mAuth.signInWithEmailAndPassword(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (task.isSuccessful()) {

                    //---Show Snackbar for Log in Successful

                    Toast.makeText(getActivity(), "Sign in Successfull", Toast.LENGTH_LONG).show();


                    setUserInformation();


                } else {
                    Toast.makeText(getActivity(), "Failed to log in", Toast.LENGTH_LONG).show();
                    Log.e("task", "" + task.getException().getMessage().toString());
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Toast.makeText(getActivity(), "Failed. Please try again later.", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void setUserInformation() {

        if (mAuth.getCurrentUser() != null) {

            User.USER_EMAIL = mAuth.getCurrentUser().getEmail().toString();

            if (mAuth.getCurrentUser().getDisplayName().toString() != null)
                User.USER_NAME = mAuth.getCurrentUser().getDisplayName().toString();


            try {

                if (mAuth.getCurrentUser().getPhotoUrl().toString() != null)
                    User.USER_PHOTO = mAuth.getCurrentUser().getPhotoUrl().toString();

            } catch (Exception e) {


            }
        }


        ((MainActivity) getActivity()).initProfile();

        getChatPage();

    }

    private void getChatPage() {

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new ChatListFragment()).addToBackStack("tag").commit();

    }


    private boolean checkValidation() {


        if (inputEmail.getText().toString().trim().isEmpty()) {

            inputEmail.setError("Email is required");
            inputEmail.requestFocus();
            return false;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {

            inputEmail.setError("Enter a valid email id");
            inputEmail.requestFocus();
            return false;
        }


        if (inputPassword.getText().toString().trim().isEmpty()) {

            inputPassword.setError("Password is required");
            inputPassword.requestFocus();
            return false;
        }

        if (inputPassword.getText().toString().trim().length() < 6) {
            inputPassword.setError("Password at least 6 letters");
            inputPassword.requestFocus();
            return false;
        }

        return true;

    }

}
