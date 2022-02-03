package Pages;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.pocketcontrol.AdHandler;

import com.example.pocketcontrol.BudgetMonthRecord;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.Item;
import com.example.pocketcontrol.R;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;

public class Analytics extends Fragment {

    private DatabaseHandler db;
    private BarChart chart;


    public Analytics() {
        super(R.layout.fragment_analytics);
    }

    @Override
    public void onStart() {
        super.onStart();

        // roll a chance from 1 to 10 to show an ad
        AdHandler adHandler = new AdHandler(this.getActivity());
        adHandler.showAdAtRandom(10);

        // activate db
        db = new DatabaseHandler(this.getContext());

        chart = this.getView().findViewById(R.id.chart1);

        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(8, false);

        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        setData(db.getAmountSpentPerMonth());
    }

    private void setData(ArrayList<BudgetMonthRecord> budgetMonthRecords) {

        ArrayList<BarEntry> values = new ArrayList<>();

        for (BudgetMonthRecord budgetMonthRecord : budgetMonthRecords) {
            values.add(new BarEntry(budgetMonthRecord.getMonthNumber(), (float) budgetMonthRecord.getAmountSpentInMonth()));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "Expenditures for past months");

            set1.setDrawIcons(false);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            chart.setData(data);
        }
    }

}