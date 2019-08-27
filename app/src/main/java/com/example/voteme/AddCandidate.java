package com.example.voteme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddCandidate extends AppCompatActivity {

    ProgressBar progressBar;
    Button add;
    public EditText candidate_name,candidate_pass;
    CircleImageView candidate_image;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    CropImage.ActivityResult result;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);
        progressBar=findViewById(R.id.add_Candidate_progress);
        progressBar.setVisibility(View.INVISIBLE);
        add=findViewById(R.id.add_Candidate);
        candidate_name=findViewById(R.id.candidate_name);
        candidate_pass=findViewById(R.id.candidate_pass);
        candidate_image=findViewById(R.id.candidate_image);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        candidate_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddCandidate.this);

            }
        });



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imageUri!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    final String Candidate_Name = candidate_name.getText().toString();
                    final String Candidate_Desc = candidate_pass.getText().toString();
                    if (Candidate_Name.isEmpty()) {
                        candidate_name.setError("Provide your Email first!");
                        candidate_name.requestFocus();
                    } else if (Candidate_Desc.isEmpty()) {
                        candidate_pass.setError("Set your password");
                        candidate_pass.requestFocus();
                    } else if (Candidate_Name.isEmpty() && Candidate_Desc.isEmpty()) {
                        Toast.makeText(AddCandidate.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                    } else if (!(Candidate_Name.isEmpty() && Candidate_Desc.isEmpty())) {

                        final StorageReference filepath=mStorageRef.child("Images")
                                .child(Candidate_Name+""+random()+".jpg");
                        filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){

                                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri uris=uri;
                                            String download_url=uri.toString();

                                            Log.e("ImageLink",download_url);

                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Candidate").push();
                                            Map<String, Object> map = new HashMap<>();
                                            map.put("id", databaseReference.getKey());
                                            map.put("Candidate_Name", Candidate_Name);
                                            map.put("Candidate_Description",Candidate_Desc);
                                            map.put("Candidate Image",download_url);
                                            databaseReference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Intent in=new Intent(AddCandidate.this, Admin.class);
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



                    } else {
                        Toast.makeText(AddCandidate.this, "Error", Toast.LENGTH_SHORT).show();

                    }


                }

            }
        });
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
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
                Log.e("Name",imageUri.toString());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
