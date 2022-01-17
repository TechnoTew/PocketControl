package com.example.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    DatabaseHandler db;
    SharedPreferenceHandler sph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        sph = new SharedPreferenceHandler(this);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);

        welcomeMessage.setText(String.format("Hi, %s", sph.getUserName()));
    }
}