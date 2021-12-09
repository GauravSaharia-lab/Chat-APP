package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.databinding.ActivityPhonenumberMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhonenumberMainActivity extends AppCompatActivity {

    ActivityPhonenumberMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPhonenumberMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth=FirebaseAuth.getInstance();

        //if user is already logged in
       if (auth.getCurrentUser()!=null){
            Intent intent= new Intent(PhonenumberMainActivity.this,MainActivity.class);
            startActivity( intent);
            finish();
        }

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhonenumberMainActivity.this,Otp_Activity.class);
                intent.putExtra("phoneNumber",binding.phoneBox.getText().toString());
                startActivity(intent);
            }
        });
    }
}