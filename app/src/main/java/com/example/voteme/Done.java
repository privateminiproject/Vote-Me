package com.example.voteme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Done extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button Logout;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        mToolBar=findViewById(R.id.appbar);
        setSupportActionBar(mToolBar);
        mToolBar.setTitle("Vote Me");

        Logout=findViewById(R.id.logout);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent=new Intent(Done.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }
}
