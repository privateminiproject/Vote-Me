package com.example.voteme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    FloatingActionButton add;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseAuth mAuth;
    String name;
    String total;
    String userEmail = null;
    FirebaseUser currentUser;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private Toolbar mToolBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar=findViewById(R.id.appbar);
        setSupportActionBar(mToolBar);
        mToolBar.setTitle("Vote Me");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

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
            userEmail = currentUser.getEmail();
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

        Query query = FirebaseDatabase.getInstance().
                getReference("Voter Email-Id").orderByChild("voter_name").equalTo(userEmail);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = dataSnapshot.getChildrenCount() + "";
                if (total.equals("1")) {
                    Intent intent = new Intent(MainActivity.this, Done.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                                        snapshot.child("id").getValue().toString(),
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
                holder.vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        myRef = database.getReference("Voter-details").child(model.getId());
                        myRef.push().setValue(name);

                        DatabaseReference dbref = database.getReference("Voter Email-Id");
                        dbref.push().child("voter_name").setValue(name);

                        Intent intent = new Intent(MainActivity.this, Done.class);
                        startActivity(intent);
                        finish();


                    }
                });

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

        };

        recyclerView.setAdapter(adapter);

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



