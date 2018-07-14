package com.example.my_pc.secretmessenger.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.CompoundButtonCompat;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatSettingActivity extends BaseActivity {


    @BindView(R.id.SCCheckBox)
    CheckBox SCCheckBox;
    @BindView(R.id.SCPassword)
    EditText SCPassword;

    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {

        setContentView(R.layout.activity_chat_setting);
        ButterKnife.bind(this);

//        firebaseDatabase = FirebaseDatabase.getInstance().getReference("https://secretmessenger-4dbf0.firebaseio.com/").child("messages");

        setTitle("Chat Setting");
        if (isSecret) {
            SCCheckBox.setChecked(true);
            SCPassword.setText(secretPass);
        } else {
            SCCheckBox.setChecked(false);
        }

        onViewClicked();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        secretPass = SCPassword.getText().toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            secretPass = SCPassword.getText().toString();
            finish();
            //   return true;
        }


      /*  if (item.getItemId() == R.id.action_delete_conversation) {

            ChatActivity.deleteConversation();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void deleteConversation() {

        String user, chatWith;

        user = User.USER_EMAIL.replace(".", "-");
        chatWith = User.CHAT_WITH_EMAIL.replace(".", "-");

        firebaseDatabase.child(user + "_" + chatWith).removeValue();

    }


    @OnClick(R.id.SCCheckBox)
    public void onViewClicked() {
        if (SCCheckBox.isChecked()) {
            isSecret = true;
            CompoundButtonCompat.setButtonTintList(SCCheckBox, ColorStateList.valueOf(Color.BLACK));
            SCCheckBox.setHighlightColor(Color.BLACK);
        } else {
            isSecret = false;
            CompoundButtonCompat.setButtonTintList(SCCheckBox, ColorStateList.valueOf(Color.BLACK));
            SCCheckBox.setHighlightColor(Color.BLACK);

        }
    }

}
