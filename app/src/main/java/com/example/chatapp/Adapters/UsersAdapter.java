package com.example.chatapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Activities.ChatActivity;
import com.example.chatapp.R;
import com.example.chatapp.Models.User;
import com.example.chatapp.databinding.RowConversationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.SimpleTimeZone;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder>{

        Context context;
        ArrayList<User>users;

    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    public UsersAdapter() {
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false);

        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        User user =users.get(position);
          String senderId= FirebaseAuth.getInstance().getUid();
          String senderRoom=senderId+user.getUid();
          FirebaseDatabase.getInstance().getReference()
                  .child("chats")
                  .child(senderRoom)
                  .addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot snapshot) {
                          if (snapshot.exists()) {
                                //updating this on main screen
                              String Msg = snapshot.child("lastMsg").getValue(String.class);
                              long time = snapshot.child("lastMsgTime").getValue(Long.class);

                              SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                              holder.binding.msgTime.setText(dateFormat.format(new Date(time)));

                             // String strLong = Long.toString(time);
                             // strLong = new SimpleDateFormat(" hh:mm").format(Calendar.getInstance().getTime());

                              holder.binding.lastmsg.setText(Msg);

                          }else {

                              holder.binding.lastmsg.setText("Tap to chat");
                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError error) {

                      }
                  });

        holder.binding.userName.setText(user.getName());
        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.profile)
                .into(holder.binding.pro);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, ChatActivity.class);
                intent.putExtra("name",user.getName());
                  intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder{

    RowConversationBinding binding;

     public UserHolder(@NonNull View itemView) {
         super(itemView);
         binding=RowConversationBinding.bind(itemView);
     }
 }
}
