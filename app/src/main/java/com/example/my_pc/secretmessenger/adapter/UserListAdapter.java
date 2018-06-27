package com.example.my_pc.secretmessenger.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.my_pc.secretmessenger.R;
import com.example.my_pc.secretmessenger.activity.ChatActivity;
import com.example.my_pc.secretmessenger.constant_values.User;
import com.example.my_pc.secretmessenger.model.UserList;
import com.example.my_pc.secretmessenger.utility.CircleTransform;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {


    private Context context;
    private List<UserList> userLists;


    public UserListAdapter(Context context, List<UserList> userLists) {
        this.context = context;
        this.userLists = userLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_view_chat_list, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserList list = userLists.get(position);

        holder.userName.setText(list.getName());

        Glide.with(context).load(list.getPhotoUrl()).placeholder(R.drawable.user).error(R.drawable.user).transform(new CircleTransform(context)).into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return userLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView userImage;
        TextView userName;

        public MyViewHolder(final View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.userImageViewId);
            userName = itemView.findViewById(R.id.userNameTextViewId);


            // item click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    User.CHAT_WITH_EMAIL = userLists.get(getAdapterPosition()).getEmail();
                    User.CHAT_WITH_NAME = userLists.get(getAdapterPosition()).getName();
                    User.CHAT_WITH_PHOTO = userLists.get(getAdapterPosition()).getPhotoUrl();

                    // Now go to the ChatActivity
                    context.startActivity(new Intent(context, ChatActivity.class));

                }
            });
        }


    }
}
