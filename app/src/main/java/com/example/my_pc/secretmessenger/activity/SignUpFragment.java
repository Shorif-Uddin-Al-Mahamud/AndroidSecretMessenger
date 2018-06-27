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
import com.example.my_pc.secretmessenger.constant_values.ConstantValues;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.example.my_pc.secretmessenger.model.UserList;
import com.example.my_pc.secretmessenger.utility.MyProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUpFragment extends Fragment {


    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    @BindView(R.id.link_login)
    TextView linkLogin;
    Unbinder unbinder;
    private View view;


    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initUI();

    }

    private void initUI() {

        getActivity().setTitle("Sign Up");
        mAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantValues.FB_DATABASE_PATH);

        progressDialog = new MyProgressDialog(getActivity()).create("Registering...", "Please wait");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_signup)
    public void onBtnSignupClicked() {

        if (!checkValidation()) {
            return;
        }


        registerUser();

    }

    @OnClick(R.id.link_login)
    public void onLinkLoginClicked() {

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new LogInFragment()).addToBackStack("tag").commit();
    }


    private boolean checkValidation() {


        if (inputName.getText().toString().trim().isEmpty()) {

            inputName.setError("Name is required");
            inputName.requestFocus();
            return false;
        }

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


    private void registerUser() {


        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();

            User.USER_EMAIL = "";
            User.USER_NAME = "";
            User.USER_PHOTO = "";
        }

        /*
         * User Registration
         */

        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {

                    Toast.makeText(getActivity(), "Register successfull", Toast.LENGTH_LONG).show();


                    Log.e("name", "" + mAuth.getCurrentUser().getEmail().toLowerCase());

                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(inputName.getText().toString().trim())
                            .build();


                    // set user name

                    mAuth.getCurrentUser().updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {


                                // if set the name successfully, then add to chat user list


                                User.USER_EMAIL = mAuth.getCurrentUser().getEmail().toString();
                                User.USER_NAME = mAuth.getCurrentUser().getDisplayName().toString();

                                addToChatList();


                            } else {

                                Toast.makeText(getActivity(), "User Name Not set. Please update your profile with name", Toast.LENGTH_LONG).show();

                            }


                        }
                    });


                } else {


                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {

                        Toast.makeText(getActivity(), "Already registered", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getActivity(), "Failed to register", Toast.LENGTH_LONG).show();
                    }

                }

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
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

    private void addToChatList() {

        UserList userList = new UserList(User.USER_NAME, User.USER_PHOTO, User.USER_EMAIL);

        databaseReference.child(User.USER_EMAIL.replace(".", "-")).setValue(userList);

    }
}
