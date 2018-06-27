package com.example.my_pc.secretmessenger.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.constant_values.FragmentId;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.example.my_pc.secretmessenger.utility.CircleTransform;
import com.example.my_pc.secretmessenger.utility.MyProgressDialog;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View headerView;
    public FirebaseAuth mAuth;
    private MyProgressDialog progressDialog;
    public static MainActivity mainActivity;
    private ImageView profilePicture;
    private TextView profileName;
    private TextView profileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();


        initVariable();


    }

    private void initVariable() {

        progressDialog = new MyProgressDialog(this);
        progressDialog.create();

        mAuth = FirebaseAuth.getInstance();

        profilePicture = headerView.findViewById(R.id.profilePictureId);
        profileName = headerView.findViewById(R.id.userNameId);
        profileEmail = headerView.findViewById(R.id.userEmailId);


        if (mAuth.getCurrentUser() != null) {

            selectedDisplay(FragmentId.CHAT_LIST);
        } else {

            selectedDisplay(FragmentId.LOGIN_ID);
        }


    }

    private void selectedDisplay(int id) {


        if (id == FragmentId.LOGIN_ID) {


            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new LogInFragment()).addToBackStack("tag").commit();

        }


        if (id == FragmentId.CHAT_LIST) {


            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new ChatListFragment()).addToBackStack("tag").commit();

        }


    }

    private void initView() {

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Sign out
        if (id == R.id.action_sign_out) {


            mAuth.signOut();

            if (mAuth.getCurrentUser() == null) {

                selectedDisplay(FragmentId.LOGIN_ID);

            }

            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {

            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chat_list) {

            // If user signed in, then go to chat list

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {

                Toast.makeText(mainActivity, "You need to sign in", Toast.LENGTH_SHORT).show();
            } else {
                selectedDisplay(FragmentId.CHAT_LIST);
            }

        } else if (id == R.id.nav_profile) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new ProfileFragment()).addToBackStack("tag").commit();

        } else if (id == R.id.nav_sign_in) {

            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new LogInFragment()).addToBackStack("tag").commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        mainActivity = this;

        if (mAuth.getCurrentUser() != null) {

            if (mAuth.getCurrentUser().getDisplayName() != null) {

                User.USER_NAME = mAuth.getCurrentUser().getDisplayName().toString();
            }

            if (mAuth.getCurrentUser().getEmail() != null) {

                User.USER_EMAIL = mAuth.getCurrentUser().getEmail().toString();
            }

            if (mAuth.getCurrentUser().getPhotoUrl() != null) {

                User.USER_PHOTO = mAuth.getCurrentUser().getPhotoUrl().toString();
            }

        }

        initProfile();
    }

    public void initProfile() {


        if (mAuth.getCurrentUser() != null) {


            profileName.setText(User.USER_NAME);
            profileEmail.setText(User.USER_EMAIL);

            Glide.with(this).load(User.USER_PHOTO).placeholder(R.drawable.user).error(R.drawable.user).transform(new CircleTransform(this)).into(profilePicture);

            profileName.setText(User.USER_NAME);
            profileEmail.setText(User.USER_EMAIL);

        }
    }
}
