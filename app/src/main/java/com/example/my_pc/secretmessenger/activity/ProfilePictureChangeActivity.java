package com.example.my_pc.secretmessenger.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.constant_values.ConstantValues;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.example.my_pc.secretmessenger.model.UserList;
import com.example.my_pc.secretmessenger.utility.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfilePictureChangeActivity extends BaseActivity {


    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;

    private static final int REQUEST_IMAGE_CAPTURE = 10101;
    private final int CHOOSE_IMAGE_REQUEST_CODE = 1010;
    @BindView(R.id.userPhotoId)
    ImageView userPhoto;
    private Uri imageUri;
    private Bitmap bitmap;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ProgressDialog progressDialog;
    private String imageDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture_change);
        ButterKnife.bind(this);

        initUI();
    }

    private void initUI() {

        setTitle("Profile Picture");

        // --- get storage reference and database reference for firebase
        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantValues.FB_DATABASE_PATH);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void selectImage() {
        Intent takeImageIntent = ImagePicker.getPickImageIntent(ProfilePictureChangeActivity.this);
        if (takeImageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeImageIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bitmap = ImagePicker.getBitmapFromResult(this, resultCode, data);
        if (null != bitmap && resultCode == Activity.RESULT_OK) {
            //do what you want with the bitmap here

            if (data.getData() != null) {

                imageUri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                    userPhoto.setImageBitmap(bitmap);
                    // Toast.makeText(getActivity(), "Okay", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    // -- Get the image extention ,, like png or jpg etc
    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @OnClick(R.id.userPhotoId)
    public void onViewClicked() {

        selectImage();
    }

    public void updatePhoto(View view) {
        if (!check()) {
            return;
        }
        // Upload to the server
        saveImageToFBServer();
    }

    private boolean check() {

        if (imageUri == null) {
            viewSnackBar("Please select an image");
            return false;
        }
        return true;
    }


    private void saveImageToFBServer() {


        if (imageUri != null) {

            progressDialog = showProgressDialog("Uploading...", "Please wait");

            StorageReference storageReference = mStorageRef.child(ConstantValues.FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imageUri));


            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageDownloadUrl = taskSnapshot.getDownloadUrl().toString();

                    // Now update user profile picture
                    updateUserProfilePicture();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    viewSnackBar("Failed! Please Try Again");

                }
            });

        }

    }

    private void updateUserProfilePicture() {

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser != null && imageDownloadUrl != null) {

                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(imageDownloadUrl)).build();


                firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            viewSnackBar("Profile Photo Updated");

                            UserList userList = new UserList();
                            userList.setEmail(firebaseUser.getEmail().toLowerCase());
                            userList.setName(firebaseUser.getDisplayName().toLowerCase());
                            userList.setPhotoUrl(imageDownloadUrl);

                            databaseReference.child(User.USER_EMAIL.replace(".", "-")).setValue(userList);


                        } else {
                            viewSnackBar("Failed to update photo");
                        }
                    }
                });
            }

        }

    }
}
