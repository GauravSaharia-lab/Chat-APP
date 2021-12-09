package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chatapp.Adapters.MessagesAdapter;
import com.example.chatapp.Models.Messages;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    MessagesAdapter messagesAdapter;
    ArrayList<Messages>messages;
    String senderRoom;
    String reciverRoom;
    FirebaseDatabase database;
  //  String saveCurrentTime, saveCurrentDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        messages= new ArrayList<>();
        messagesAdapter = new MessagesAdapter(this,messages);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setAdapter(messagesAdapter);

         //Tempo
       /* Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());*/


        String name= getIntent().getStringExtra("name");
        String reciverUid= getIntent().getStringExtra("uid");
        //taking loogend in user  id
        String senderUid= FirebaseAuth.getInstance().getUid();

        senderRoom=senderUid+ reciverUid;
        reciverRoom = reciverUid+senderUid;

            database= FirebaseDatabase.getInstance();
            database.getReference().child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           messages.clear();

                           for (DataSnapshot snapshot1:snapshot.getChildren()){

                           Messages messaes=snapshot1.getValue(Messages.class);
                           messages.add(messaes);

                           }
                           messagesAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        binding.sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt=binding.msgbox.getText().toString();
                if (messageTxt.isEmpty()){
                       binding.msgbox.setError("Type a messages");
                       return;
                    //Toast.makeText(ChatActivity.this, "enter something", Toast.LENGTH_SHORT).show();

                }
               Date date = new Date();

                Messages message= new Messages(messageTxt,senderUid,date.getTime());
                binding.msgbox.setText("");

                String randomkey=database.getReference().push().getKey();

                //last messages show
                HashMap<String,Object>lastmesg= new HashMap<>();
                lastmesg.put("lastMsg",message.getMessage());
                lastmesg.put("lastMsgTime",date.getTime());

                database.getReference().child("chats").child(senderRoom).updateChildren(lastmesg);
                database.getReference().child("chats").child(reciverRoom).updateChildren(lastmesg);

                /*
                HashMap<String,Object>lastmesgTime= new HashMap<>();
                lastmesg.put("lastMsgTime",date.getTime());
                database.getReference().child("chats").child(senderRoom).updateChildren(lastmesgTime);
                database.getReference().child("chats").child(reciverRoom).updateChildren(lastmesgTime);*/




                database.getReference().child("chats")
                        .child(senderRoom).child("messages")
                        .child(randomkey)

                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .child(randomkey)
                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });

                    }
                });
            }
        });

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}