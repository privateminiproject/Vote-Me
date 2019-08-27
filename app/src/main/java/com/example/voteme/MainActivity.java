package com.example.voteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Intent intent3 = getIntent();
        String name = intent3.getStringExtra("Email");
        Log.e("Current","Current User is = "+name);

        if ( name== null) {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();

            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        else{

            if (name.equals("admin@gmail.com")){
                Intent intent=new Intent(MainActivity.this,Admin.class);
                startActivity(intent);
                finish();
            }
            else{


//                recyclerView = findViewById(R.id.list);
//                linearLayoutManager = new LinearLayoutManager(this);
//                recyclerView.setLayoutManager(linearLayoutManager);
//                recyclerView.setHasFixedSize(true);
//                fetch();
            }


        }


    }
}
