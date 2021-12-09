package com.example.chatapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatapp.Models.User;
import com.example.chatapp.databinding.ActivitySetUpprofileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetUpprofileActivity extends AppCompatActivity {

    ActivitySetUpprofileBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedItem;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding=ActivitySetUpprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog=new ProgressDialog(this);
        dialog.setMessage("Updating profile ");
        dialog.setCancelable(false);
           database=FirebaseDatabase.getInstance();
           storage=FirebaseStorage.getInstance();
         auth=FirebaseAuth.getInstance();


         //Selecting image  from storgae
        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
             startActivityForResult(intent,45);
            }
        });
        //storing profile image to firebase
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=binding.Box.getText().toString();

                if (name.isEmpty()){

                    binding.Box.setError("Please type a name ");
                    return;
                }
               dialog.show();

                if (selectedItem!=null){

                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());

                    //Selected from OnActivityResults
                    reference.putFile(selectedItem).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()){

                                //after uploadinh the image a link generates we have to get that
                                //getUrl will give us a uri a path we have to convert that uti to String

                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl= uri.toString();

                                        String uid=auth.getUid();
                                        String phone=auth.getCurrentUser().getPhoneNumber();
                                        String name=binding.Box.getText().toString();

                                        User user=new User(uid,name,phone,imageUrl);

                                        database.getReference()
                                                .child("user")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        dialog.dismiss();
                                                        Intent intent= new Intent(SetUpprofileActivity.this,MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                    }
                                });
                            }

                        }
                    });


                }
                else {


                    String uid=auth.getUid();
                    String phone=auth.getCurrentUser().getPhoneNumber();


                    User user=new User(uid,name,phone,"No image");

                    database.getReference()
                            .child("user")
                            .child(uid)
                            .setValue(user)
                             .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Intent intent= new Intent(SetUpprofileActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data !=null){
          if (data.getData() !=null){
       binding.imageView3.setImageURI(data.getData());

       selectedItem=data.getData();

          }

        }
    }
}