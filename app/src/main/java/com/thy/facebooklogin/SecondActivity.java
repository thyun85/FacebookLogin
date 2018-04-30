package com.thy.facebooklogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    TextView tvEmail, tvBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tvEmail = findViewById(R.id.tv_email);
        tvBirth = findViewById(R.id.tv_birthday);

        Intent intent = getIntent();

        tvEmail.setText(intent.getStringExtra("email"));
        tvBirth.setText(intent.getStringExtra("birthday"));
//        getData();
    }

//    void getData(){
//        Intent intent = getIntent();
//
//        tvEmail.setText(intent.getStringExtra("email"));
//        tvBirth.setText(intent.getStringExtra("birthday"));
//    }
}
