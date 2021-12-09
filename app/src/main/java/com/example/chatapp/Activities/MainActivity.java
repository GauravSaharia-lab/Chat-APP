package com.example.chatapp.Activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.chatapp.R;
import com.example.chatapp.Models.User;
import com.example.chatapp.Adapters.UsersAdapter;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
       ActivityMainBinding binding;
       //Taking data from firebase to recview
       FirebaseDatabase database;
       ArrayList<User> users;
       UsersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


       //fetching data
        database=FirebaseDatabase.getInstance();
        users=new ArrayList<>();
      adapter= new UsersAdapter(this,users);
      binding.recview.setAdapter(adapter);
        Log.i(TAG, "onCreate: hh");
      //showing data in recview
        database.getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              users.clear();
               for (DataSnapshot snapshot1: snapshot.getChildren()){

                   User user6=snapshot1.getValue(User.class);
                   if(!user6.getUid().equals(FirebaseAuth.getInstance().getUid()))
                       users.add(user6);

                   Log.i(TAG, "onDataChange: ho gya");
               }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

      //Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.search:
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            break;

            case R.id.settings:
                Toast.makeText(this, "Settings  clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}