package com.example.voteme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    BottomAppBar bottomAppBar;
    FloatingActionButton add;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private LinearLayoutManager linearLayoutManager;
    String id = null;
    String name = null;
    String image = null;
    String description = null;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        add = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.list1);


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();
        bottomAppBar.setVisibility(View.INVISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && !add.isShown()) {
                    bottomAppBar.setVisibility(View.INVISIBLE);
                    add.show();

                } else if (dy > 0 && add.isShown()) {
                    add.hide();
                    bottomAppBar.setVisibility(View.INVISIBLE);
                }
            }

        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, AddCandidate.class);
                startActivity(intent);


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
            protected void onBindViewHolder(final ViewHolder holder, final int position, final Model model) {

                holder.setTxtTitle(model.getName());
                holder.setTxtDesc(model.getDiscription());
                Glide.with(Admin.this).load(model.getImage()).into(holder.img);


                DatabaseReference candidate_name = database.getReference("Voter-details").child(model.getName());
                candidate_name.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Employee1Count = dataSnapshot.getChildrenCount() + " Voted";
                        holder.vote.setText(Employee1Count);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        id = model.getId();
                        name = model.getName();
                        image = model.getImage();
                        description = model.getDiscription();
                        Intent intent = new Intent(Admin.this, Profile.class);
                        intent.putExtra("name", name);
                        intent.putExtra("id", id);
                        intent.putExtra("image", image);
                        intent.putExtra("description", description);
                        startActivity(intent);


                    }
                });

            }

        };
        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_vote:
                DatabaseReference candidate_name = database.getReference("Voter-details");
                DatabaseReference Voter_id = database.getReference("Voter Email-Id");
                candidate_name.removeValue();
                Voter_id.removeValue();
                return true;


            case R.id.logout:
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent2 = new Intent(Admin.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
