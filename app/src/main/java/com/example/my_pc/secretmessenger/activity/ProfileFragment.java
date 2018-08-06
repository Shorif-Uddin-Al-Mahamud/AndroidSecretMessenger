package com.example.my_pc.secretmessenger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.my_pc.secretmessenger.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {

    Unbinder unbinder;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();

    }

    private void initUI() {

        getActivity().setTitle("My Profile");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.updateProfilePictureBtn)
    public void onUpdateProfilePictureBtnClicked() {

        startActivity(new Intent(getActivity(), ProfilePictureChangeActivity.class));
    }

    @OnClick(R.id.updateUserNameBtn)
    public void onUpdateUserNameBtnClicked() {

        startActivity(new Intent(getActivity(), ChangeUserNameActivity.class));
    }

    @OnClick(R.id.changePasswordBtn)
    public void onChangePasswordBtnClicked() {
        startActivity(new Intent(getActivity(), ChangePasswordActivity.class));

    }
}
