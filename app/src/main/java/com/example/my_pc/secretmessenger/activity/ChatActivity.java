package com.example.my_pc.secretmessenger.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private LinearLayout layout;
    private ScrollView scrollView;
    private EditText messageArea;

    private Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initUI();

        initVariable();

        setTheMessage();
    }


    private void initVariable() {

        layout = findViewById(R.id.layoutId);
        scrollView = findViewById(R.id.scrollViewId);
        messageArea = findViewById(R.id.messageArea);

        //scrollView.fullScroll(View.FOCUS_DOWN);


        String user = User.USER_EMAIL.replace(".", "-");
        String chatWith = User.CHAT_WITH_EMAIL.replace(".", "-");

        reference1 = new Firebase("https://secretmessenger-4dbf0.firebaseio.com/messages/" + user + "_" + chatWith);
        reference2 = new Firebase("https://secretmessenger-4dbf0.firebaseio.com/messages/" + chatWith + "_" + user);
    }

    private void initUI() {


        // Load the UI
        setContentView(R.layout.activity_chat);


        // Firebase Client

        Firebase.setAndroidContext(this);


        try {

            setTitle(User.CHAT_WITH_NAME);

        } catch (Exception e) {
            setTitle("Chat");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_settings) {

            startActivity(new Intent(this, ChatSettingActivity.class));
            return true;
        }

        // If menu option is selected
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setIcon(R.drawable.ic_warning).setTitle("Exit?").setMessage("Are you want to exit from chat?").setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // exit from chat activity
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // do nothing here
            }
        }).create().show();


        //super.onBackPressed();
    }


    private void setTheMessage() {

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();


                // Decript  message from here


                if (userName.equals(User.USER_EMAIL)) {

                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox(User.CHAT_WITH_NAME + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void addMessageBox(String message, int type) {

        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.weight = 1;

        if (type == 1) {
            layoutParams.gravity = Gravity.LEFT;
            textView.setBackgroundColor(Color.YELLOW);
        } else {
            layoutParams.gravity = Gravity.RIGHT;
            textView.setBackgroundColor(Color.GREEN);
        }

        layoutParams.setMargins(10, 10, 10, 10);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(20);
        textView.setPadding(10, 10, 10, 10);

        layout.addView(textView);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);

        // ---- test
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 1000);


    }


    public void sendMessage(View view) {

        String messageText = messageArea.getText().toString().trim();

        // Encript message from here and then send

        if (!messageText.equals("")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", messageText);
            map.put("user", User.USER_EMAIL);
            reference1.push().setValue(map);
            reference2.push().setValue(map);
            messageArea.setText("");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        // setTheMessage();
    }


}