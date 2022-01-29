package Pages;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;

import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.Item;
import com.example.pocketcontrol.LastSpendingRecordItem;
import com.example.pocketcontrol.LastSpendingRecordItemArrayAdapter;
import com.example.pocketcontrol.R;
import com.example.pocketcontrol.SharedPreferenceHandler;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


public class Overview extends Fragment {

    private DatabaseHandler db;
    private SharedPreferenceHandler sph;
    private PieChart chart;
    private RecyclerView lastSpendingRecordsView;
    private double totalAmountSpent;


    public Overview() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sph = new SharedPreferenceHandler(this.getContext());
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        TextView welcomeMessage = (TextView) view.findViewById(R.id.welcomeMessage);

        welcomeMessage.setText(String.format("Hi, %s", sph.getUserName()));

        db = new DatabaseHandler(this.getContext());

        ArrayList<Category> categoriesWithItemTotals = db.getAllCategoriesWithItemTotals();

        // initialize pie chart
        chart = view.findViewById(R.id.pieChart);
        this.renderPieChart(chart, categoriesWithItemTotals, 500);

        ArrayList<LastSpendingRecordItem> lastSpendingRecordItemArrayList = new ArrayList<LastSpendingRecordItem>();
        for (Item item: db.getAllItems(true)) {
            lastSpendingRecordItemArrayList.add(new LastSpendingRecordItem(item.getItemName(), item.getItemValue()));
        }
        generateUIForRecycleView(view, lastSpendingRecordItemArrayList);

        return view;
    }

    public void generateUIForRecycleView(View view, ArrayList<LastSpendingRecordItem> lastSpendingRecordItems) {
        // Reference of RecyclerView
        lastSpendingRecordsView = view.findViewById(R.id.lastSpendingRecordView);

        // Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
        // Set Layout Manager to RecyclerView
        lastSpendingRecordsView.setLayoutManager(linearLayoutManager);

        // Create adapter
        LastSpendingRecordItemArrayAdapter myRecyclerViewAdapter = new LastSpendingRecordItemArrayAdapter(lastSpendingRecordItems, new LastSpendingRecordItemArrayAdapter.lastSpendingRecordItemClickListener()
        {

            @Override
            public void onItemClicked(LastSpendingRecordItem item) {
                Log.d("", item.getItemName());
            }
        });

        // Set adapter to RecyclerView
        lastSpendingRecordsView.setAdapter(myRecyclerViewAdapter);
    }

    private void renderPieChart(PieChart pieChart, ArrayList<Category> categoriesWithItemTotals, int animationDurationMillis) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        double totalAmountSpent = categoriesWithItemTotals.stream().mapToDouble(category -> category.getTotalValueSpentInCategory()).sum();

        // adding the items with their categories as labels
        for (Category category : categoriesWithItemTotals) {
            // if category is empty skip adding it into the pie chart
            if (category.getTotalValueSpentInCategory() <= 0) continue;

            // if category percentage rounds down to 0, skip adding it too
            System.out.println(category.getMaxValueToSpendInCategory());
            if (Math.floor(category.getTotalValueSpentInCategory() / totalAmountSpent * 100) == 0) continue;

            entries.add(new PieEntry((float) category.getTotalValueSpentInCategory(), category.getCategoryName()));
        }

        pieChart.setExtraOffsets(10, 10, 10, 10);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(String.format("Amount Spent:\n$%.2f", totalAmountSpent));
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setCenterTextSize(14);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setUsePercentValues(true);

        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setEntryLabelTextSize(13);
        pieChart.setMinAngleForSlices(25);

        // Set data in pie chart
        PieDataSet dataSet = new PieDataSet(entries, "Budget Overview");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(10f);

        // Outside values
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100f); // When valuePosition is OutsideSlice, indicates offset as percentage out of the slice size */
        dataSet.setValueLinePart1Length(0.3f); // When valuePosition is OutsideSlice, indicates length of first half of the line */
        dataSet.setValueLinePart2Length(0.3f); // When valuePosition is OutsideSlice, indicates length of second half of the line */

        // Legend attributes
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        // add only material colours into the pie chart
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);

        chart.invalidate();

        // animate the chart
        chart.animateY(animationDurationMillis);
    }
}