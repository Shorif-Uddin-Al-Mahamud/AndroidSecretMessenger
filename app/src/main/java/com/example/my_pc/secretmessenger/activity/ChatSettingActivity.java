package com.example.my_pc.secretmessenger.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.my_pc.secretmessenger.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatSettingActivity extends BaseActivity {


    @BindView(R.id.SCCheckBox)
    CheckBox SCCheckBox;
    @BindView(R.id.SCPassword)
    EditText SCPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {

        setContentView(R.layout.activity_chat_setting);
        ButterKnife.bind(this);

        setTitle("Chat Setting");
        if (isSecret) {
            SCCheckBox.setChecked(true);
            SCPassword.setText(secretPass);
        } else {
                SCCheckBox.setChecked(false);
        }
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
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.SCCheckBox)
    public void onViewClicked() {
        if (SCCheckBox.isChecked()) {
            isSecret = true;
        } else {
            isSecret = false;
        }
    }

}
