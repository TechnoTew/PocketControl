package Pages;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;


import com.example.pocketcontrol.AdHandler;

import com.example.pocketcontrol.BudgetDayRecord;
import com.example.pocketcontrol.BudgetMonthRecord;
import com.example.pocketcontrol.BudgetMonthRecordArrayAdapter;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.R;
import com.example.pocketcontrol.ThemeManager;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class Analytics extends Fragment {

    private DatabaseHandler db;
    private LineChart chart;
    private RecyclerView expenditureHistoryData;
    private int chartColour;
    private int chartInfoColour;

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

        if (ThemeManager.isDarkTheme(this.getView())) {
            chartColour = ContextCompat.getColor(this.getContext(), R.color.Yellow);
            chartInfoColour = ContextCompat.getColor(this.getContext(), R.color.White);
        } else {
            chartColour = ContextCompat.getColor(this.getContext(), R.color.Blue);
            chartInfoColour = ContextCompat.getColor(this.getContext(), R.color.Black);
        }

        generateUIForRecycleView(this.getView(), db.getAmountSpentPerMonth(true));
        
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
        xAxis.setTextColor(this.chartInfoColour);
        xAxis.setAxisLineColor(this.chartInfoColour);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.disableAxisLineDashedLine();
        rightAxis.disableGridDashedLine();
        rightAxis.setLabelCount(8, false);

        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setLabelCount(6, false);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.disableAxisLineDashedLine();
        leftAxis.disableGridDashedLine();
        leftAxis.setTextColor(this.chartInfoColour);
        leftAxis.setAxisLineColor(this.chartInfoColour);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setXOffset(-25f);
        l.setTextColor(this.chartInfoColour);

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
            dataSet.setColor(this.chartColour);
            dataSet.setCircleColor(this.chartColour);
            dataSet.setFillColor(this.chartColour);
            dataSet.setDrawHorizontalHighlightIndicator(false);
            dataSet.setDrawVerticalHighlightIndicator(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSet);

            LineData data = new LineData(dataSets);
            data.setValueTextSize(10f);

            chart.setData(data);
        }
    }

    private void generateUIForRecycleView(View view, ArrayList<BudgetMonthRecord> budgetMonthRecordArrayList) {
        // reference of recyclerView
        expenditureHistoryData = view.findViewById(R.id.budgetMonthRecycleView);

        // Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);

        // Set Layout Manager to RecyclerView
        expenditureHistoryData.setLayoutManager(linearLayoutManager);

        BudgetMonthRecordArrayAdapter budgetMonthRecordArrayAdapter = new BudgetMonthRecordArrayAdapter(budgetMonthRecordArrayList, budgetItem -> {

        });

        // set the adapter
        expenditureHistoryData.setAdapter(budgetMonthRecordArrayAdapter);
    }
}