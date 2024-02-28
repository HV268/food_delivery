package com.example.foody_express;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class profile extends AppCompatActivity {

    TextView hmail , hname , hnumber , hrest , hedit;
    FirebaseAuth hFire;
    FirebaseFirestore hStore;
    String hUserId;
    ImageView hprofile;
    StorageReference hstorage;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        hname = findViewById(R.id.namedata);
        hmail = findViewById(R.id.maildata);
        hnumber = findViewById(R.id.phonedata);
        hrest = findViewById(R.id.resetpass);
        hedit = findViewById(R.id.editphoto);
        hprofile =findViewById(R.id.profileimage);
        hFire =FirebaseAuth.getInstance();
        hStore = FirebaseFirestore.getInstance();
        hstorage = FirebaseStorage.getInstance().getReference();


        hUserId = hFire.getCurrentUser().getUid();


        DocumentReference documentReference = hStore.collection("users").document(hUserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
              hname.setText(value.getString("fname"));
              hmail.setText(value.getString("email"));
               hnumber.setText(value.getString("phone"));
            }
        });

        hedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openimage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openimage,1000);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode , @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode , resultCode , data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri imageuri = data.getData();
                hprofile.setImageURI(imageuri);
                uploadImage(imageuri);

            }
        }
    }

    private void uploadImage(Uri imageuri) {

        StorageReference fileref= hstorage.child("profile.jpg");
        fileref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(profile.this,"Image Uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this,"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }






}

