package com.example.my_pc.secretmessenger.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.aes.AES;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import static com.example.my_pc.secretmessenger.R.drawable.rounded_background;

public class ChatActivity extends BaseActivity {


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


                if (userName.equals(User.USER_EMAIL)) {

                    String title = "<strong><b><i>Me: </i></b></strong>";
                    addMessageBox(title, message, 1);
                } else {
                    String title = "<strong><b><i>" + User.CHAT_WITH_NAME + ": </i></b></strong>";
                    addMessageBox(title, message, 2);
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


    private void addMessageBox(final String title, final String message, int type) {

        final TextView textView = new TextView(ChatActivity.this);

        textView.setText(Html.fromHtml(title));
        textView.append("\n");
        textView.append(message);
        textView.setBackgroundResource(R.drawable.rounded_background);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //layoutParams.weight = 1;

        if (type == 1) {
            layoutParams.gravity = Gravity.LEFT;
            textView.setBackgroundColor(Color.parseColor("#263238"));
            textView.setBackgroundResource(R.drawable.rounded_background);
          //  textView.setPadding(14, 3, 4, 3);
            textView.setTextColor(Color.WHITE);
        } else {
            layoutParams.gravity = Gravity.RIGHT;
            textView.setBackgroundColor(Color.parseColor("#CFD8DC"));
            textView.setBackgroundResource(R.drawable.rounded_background2);
           // textView.setPadding(14, 3, 4, 3);
            textView.setTextColor(Color.BLACK);
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
        }, 100);


        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // test
                //  Toast.makeText(ChatActivity.this, "" + message, Toast.LENGTH_LONG).show();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChatActivity.this);
                LayoutInflater inflater = ChatActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.get_key_alert_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edittext_key);

                // dialogBuilder.setTitle("Enter Key");
                dialogBuilder.setMessage("Enter Key");
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do something with edt.getText().toString();
                        String secretPass2 = edt.getText().toString().trim();
                        String msg = message;
                        // originalMessage = msg.replace(User.CHAT_WITH_NAME + ":-", "").trim();

                        String decryptMessage = AES.decrypt(msg, secretPass2);
                        textView.setText(Html.fromHtml(title));
                        textView.append("\n");
                        if (decryptMessage != null || decryptMessage != "") {

                            try {
                                textView.append(decryptMessage);

                                // delay 10 second
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        // Actions to do after 10 seconds
                                        textView.append(message);
                                    }
                                }, 10000);

                                // end of delay
                            } catch (Exception e) {
                                textView.append(message);
                            }
                        } else {
                            textView.append(message);
                        }
                       /* if (msg.contains("You:")) {
                            msg = msg.replace("You" + ":", "").trim();
                            decriptMessage = AES.decrypt(msg, secretPass);
                            textView.setText("You" + ":\n" + decriptMessage);
                            // textView.setText();

                        } else {
                            msg = msg.replace(User.CHAT_WITH_NAME + ":", "").trim();
                            decriptMessage = AES.decrypt(msg, secretPass);
                            textView.setText(User.CHAT_WITH_NAME + ":\n" + decriptMessage);
                        }*/

                        //Toast.makeText(ChatActivity.this, "" + msg, Toast.LENGTH_LONG).show();
                    }
                });

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass

                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

                return true;
            }
        });


    }


    public void sendMessage(View view) {

        String messageText = messageArea.getText().toString().trim();

        // Encript message from here and then send
        if (isSecret) {
            // AES.setKey(secretPass);
            messageText = AES.encrypt(messageText, secretPass);
        }

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