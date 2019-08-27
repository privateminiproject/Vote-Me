package com.example.voteme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class AddCandidate extends AppCompatActivity {

    ProgressBar progressBar;
    Button add;
    public EditText candidate_name,candidate_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);
        progressBar=findViewById(R.id.loginProgressBars);
        progressBar.setVisibility(View.INVISIBLE);
        add=findViewById(R.id.add_Candidate);
        candidate_name=findViewById(R.id.candidate_name);
        candidate_pass=findViewById(R.id.candidate_pass);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Candidate").push();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", databaseReference.getKey());
                    map.put("Candidate_Name", Candidate_Name);
                    map.put("Candidate_Description",Candidate_Desc);
                    map.put("Candidate Image",Candidate_Desc);
                    databaseReference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent in=new Intent(AddCandidate.this, Admin.class);
                            startActivity(in);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });


                } else {
                    Toast.makeText(AddCandidate.this, "Error", Toast.LENGTH_SHORT).show();

                }


            }
        });





    }
}
