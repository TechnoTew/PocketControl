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

import com.example.pocketcontrol.BudgetDayRecord;
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
import java.util.Calendar;

public class Analytics extends Fragment {

    private DatabaseHandler db;
    private LineChart chart;


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


        for (BudgetDayRecord budgetDayRecord : db.getAmountSpentPerDay(true)) {
            System.out.println(budgetDayRecord);
        }

        chart = this.getView().findViewById(R.id.lineChart);


        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.setDrawGridBackground(false);
        // chart.setDrawYLabels(false);

        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);
        chart.setMinimumHeight(600);
        chart.animateXY(0, 1000);
        chart.setExtraTopOffset(10f);
        chart.setNoDataText("No Data Available");

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(R.color.black);
        xAxis.setAxisLineColor(R.color.black);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.enableGridDashedLine(10f, 10f, 10f);
        rightAxis.setLabelCount(8, false);

        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.enableGridDashedLine(10f, 10f, 10f);
        leftAxis.setTextColor(R.color.DarkRed);
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(6, false);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setXOffset(-25f);

        setData(db.getAmountSpentPerDay(true));
    }

    private void setData(ArrayList<BudgetDayRecord> budgetDayRecords) {

        ArrayList<Entry> values = new ArrayList<>();

        for (BudgetDayRecord budgetDayRecord : budgetDayRecords) {

            values.add(new Entry((float) budgetDayRecord.getDayNumber(), (float) budgetDayRecord.getAmountSpentInDay()));
        }

        LineDataSet dataSet;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            dataSet = (LineDataSet) chart.getData().getDataSetByIndex(0);
            dataSet.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            dataSet = new LineDataSet(values, "Expenditures for this month");

            dataSet.setDrawFilled(true);
            dataSet.setFillAlpha(45);
            dataSet.setDrawIcons(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet.setCubicIntensity(0.15f);
            dataSet.setDrawValues(false);
            dataSet.setDrawCircleHole(false);
            dataSet.setColor(R.color.black);
            dataSet.setCircleColor(R.color.black);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            LineData data = new LineData(dataSets);
            data.setValueTextSize(10f);

            chart.setData(data);
        }
    }

}