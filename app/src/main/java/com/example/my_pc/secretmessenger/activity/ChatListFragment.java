package com.example.my_pc.secretmessenger.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.adapter.UserListAdapter;
import com.example.my_pc.secretmessenger.constant_values.ConstantValues;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.example.my_pc.secretmessenger.model.UserList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private View view;

    private DatabaseReference databaseReference;
    private List<UserList> userLists;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initUI();

        initVariables();


    }

    private void getUsersFromFB() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userLists.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    UserList list = snapshot.getValue(UserList.class);
                    if (!list.getEmail().equals(User.USER_EMAIL))
                        userLists.add(list);
                }


                //   Log.e("name 1", "" + userLists.get(1).getName());


                UserListAdapter adapter = new UserListAdapter(getContext(), userLists);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initVariables() {

        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantValues.FB_DATABASE_PATH);
        userLists = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_chat_list_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getUsersFromFB();

    }

    private void initUI() {


        // If user not sign in, then go to sign in page
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new LogInFragment()).addToBackStack("tag").commit();
            return;
        }

        getActivity().setTitle("Chat List");


    }

}
