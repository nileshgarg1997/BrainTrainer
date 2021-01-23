package com.myapp.braintrainerpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class Rules extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);


    }

    public void PLAY(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}