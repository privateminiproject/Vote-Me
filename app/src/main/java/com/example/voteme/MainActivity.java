package com.example.voteme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    FloatingActionButton add;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth mAuth;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        Intent intent3 = getIntent();
        name = intent3.getStringExtra("Email");
        Log.e("Current", "Current User is = " + name);

        if (name == null) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

            Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent2);
            finish();
        } else {

            if (name.equals("admin@gmail.com")) {
                Intent intent = new Intent(MainActivity.this, Admin.class);
                startActivity(intent);
                finish();
            } else {
                recyclerView = findViewById(R.id.list);
                linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                fetch();
            }


        }

    }


    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Candidate");

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(query, new SnapshotParser<Model>() {
                            @NonNull
                            @Override
                            public Model parseSnapshot(@NonNull DataSnapshot snapshot) {

//                                return new Model(snapshot.getKey());


                                return new Model(
                                        snapshot.child("Candidate_Name").getValue().toString(),
                                        snapshot.child("Candidate_Description").getValue().toString(),
                                        snapshot.child("Candidate Image").getValue().toString());

                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item, parent, false);
                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, final Model model) {
                holder.setTxtTitle(model.getName());
                holder.setTxtDesc(model.getDiscription());
                Glide.with(MainActivity.this).load(model.getImage()).into(holder.img);

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };

        if (name.equals("admin@gmail.com")){

        }
        else {
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}



