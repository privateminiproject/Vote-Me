package com.example.voteme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    ProgressBar progressBar;
    Button add, delete;
    public EditText candidate_name, candidate_Disc;
    CircleImageView candidate_image;
    private Uri imageUri;
    private StorageReference mStorageRef;
    String Email;
    ArrayList<String> arrayList = new ArrayList<>();
    String names, desc, id, images;

    int del;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressBar = findViewById(R.id.add_Candidate_progress);
        progressBar.setVisibility(View.INVISIBLE);
        add = findViewById(R.id.profile_add_Candidate);
        delete = findViewById(R.id.profile_Delete_Candidate);
        candidate_name = findViewById(R.id.profile_candidate_name);
        candidate_Disc = findViewById(R.id.profile_candidate_pass);
        candidate_image = findViewById(R.id.profile_candidate_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Intent intent3 = getIntent();
        names = intent3.getStringExtra("name");
        id = intent3.getStringExtra("id");
        images = intent3.getStringExtra("image");
        desc = intent3.getStringExtra("description");


        candidate_name.setText(names);
        candidate_Disc.setText(desc);
        Glide.with(Profile.this).load(images).into(candidate_image);

        candidate_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(Profile.this);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Candidate_Name = candidate_name.getText().toString();
                final String Candidate_Desc = candidate_Disc.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                candidate_image.setOnClickListener(null);
                candidate_name.setFocusable(false);
                candidate_Disc.setFocusable(false);
                if (imageUri == null) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Candidate").child(id);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("Candidate_Name", Candidate_Name);
                    map.put("Candidate_Description", Candidate_Desc);
                    map.put("Candidate Image", images);
                    databaseReference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent in = new Intent(Profile.this, Admin.class);
                            startActivity(in);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }


                if (imageUri != null) {

                    final StorageReference filepath = mStorageRef.child("Images")
                            .child(Candidate_Name + "" + random() + ".jpg");
                    filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri uris = uri;
                                        String download_url = uri.toString();
                                        Log.e("ImageLink", download_url);
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Candidate").child(id);
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("id", id);
                                        map.put("Candidate_Name", Candidate_Name);
                                        map.put("Candidate_Description", Candidate_Desc);
                                        map.put("Candidate Image", download_url);
                                        databaseReference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent in = new Intent(Profile.this, Admin.class);
                                                startActivity(in);
                                                finish();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Voter-details");
        databaseReference.child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                arrayList.add(dataSnapshot.getValue().toString());
                Log.e("Values", arrayList.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                candidate_image.setImageURI(imageUri);
                Log.e("Name", imageUri.toString());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void Delete(View view) {

        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Voter-details").child(id);
        reference1.removeValue();

        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference("Candidate").child(id);
        reference2.removeValue();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Voter Email-Id");
        for (int i = 0; i < arrayList.size(); i++) {
            Email = arrayList.get(i);
            Log.e("Email",Email);

            reference.orderByChild("voter_name").equalTo(Email).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    dataSnapshot.getRef().setValue(null);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        Intent intent=new Intent(Profile.this,Admin.class);
        startActivity(intent);
        finish();


//        DatabaseReference deleteRef=FirebaseDatabase.getInstance().getReference();
//        deleteRef.child(KeyName).removeValue();

    }
}
