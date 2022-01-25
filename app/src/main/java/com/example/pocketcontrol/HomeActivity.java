package com.example.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        BottomNavigationView botNav = findViewById(R.id.bottomNavigationView);
        botNav.setOnItemSelectedListener(navListener);
        botNav.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        botNav.setItemHorizontalTranslationEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView4, new Overview()).commit();

    }

    private final BottomNavigationView.
            OnItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.overview:
                selectedFragment = new Overview();
                this.setTitle("Overview");
                break;
            case R.id.spendings:
                selectedFragment = new Spendings();
                this.setTitle("Spendings");
                break;
            case R.id.budgeting:
                selectedFragment = new Budgeting();
                this.setTitle("Budgeting");
                break;
            case R.id.settings:
                selectedFragment = new Settings();
                this.setTitle("Settings");
                break;

        }
        // It will help to replace the
        // one fragment to other.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView4, selectedFragment)
                .commit();
        return true;
    };
}