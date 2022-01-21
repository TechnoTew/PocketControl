package com.example.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    DatabaseHandler db;
    SharedPreferenceHandler sph;

    private PieChart chart;

    RecyclerView lastSpendingRecordsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        sph = new SharedPreferenceHandler(this);
        TextView welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);

        welcomeMessage.setText(String.format("Hi, %s", sph.getUserName()));

        db = new DatabaseHandler(this);

        ArrayList<Category> categoriesWithItemTotals = db.getAllCategoriesWithItemTotals();
        // initialize pie chart
        chart = findViewById(R.id.pieChart);
        renderPieChart(chart, categoriesWithItemTotals, 500);

        ArrayList<LastSpendingRecordItem> lastSpendingRecordItemArrayList = new ArrayList<LastSpendingRecordItem>();
        for (Item item: db.getRecent5Items()) {
            lastSpendingRecordItemArrayList.add(new LastSpendingRecordItem(item.getItemName(), item.getItemValue()));
        }
        generateUIForRecycleView(lastSpendingRecordItemArrayList);
    }

    public void generateUIForRecycleView(ArrayList<LastSpendingRecordItem> lastSpendingRecordItems) {
        //Reference of RecyclerView
        lastSpendingRecordsView = findViewById(R.id.lastSpendingRecordView);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        lastSpendingRecordsView.setLayoutManager(linearLayoutManager);

        //Create adapter
        LastSpendingRecordItemArrayAdapter myRecyclerViewAdapter = new LastSpendingRecordItemArrayAdapter(lastSpendingRecordItems, new LastSpendingRecordItemArrayAdapter.MyRecyclerViewItemClickListener()
        {

            @Override
            public void onItemClicked(LastSpendingRecordItem item) {
                Log.d("", item.getItemName());
            }
        });

        //Set adapter to RecyclerView
        lastSpendingRecordsView.setAdapter(myRecyclerViewAdapter);

    }

    private void renderPieChart(PieChart pieChart, ArrayList<Category> categoriesWithItemTotals, int animationDurationMillis) {
        pieChart.getDescription().setEnabled(false);
        //pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setCenterText("Budget Overview");
        pieChart.setUsePercentValues(true);

        ArrayList<PieEntry> entries = new ArrayList<>();

        // adding the items with their categories as labels
        for (Category category : categoriesWithItemTotals) {
            entries.add(new PieEntry((float) category.getTotalValueInCategory(), category.getCategoryName()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Budget Overview");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(10f);
        pieChart.setDrawEntryLabels(false);

        //legend attributes
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12);
        legend.setFormSize(20);
        legend.setFormToTextSpace(2);


        // add only material colours into the pie chart
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        chart.invalidate();
        // animate the chart
        chart.animateY(animationDurationMillis);
    }

}