package com.example.pocketcontrol;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import java.text.DateFormatSymbols;

public class BudgetMonthRecordArrayAdapter extends RecyclerView.Adapter<BudgetMonthRecordArrayAdapter.BudgetMonthRecordViewHolder> {

    private ArrayList<BudgetMonthRecord> budgetMonthRecordArrayList;
    private BudgetMonthRecordClickListener budgetMonthRecordClickListener;

    public BudgetMonthRecordArrayAdapter(ArrayList<BudgetMonthRecord> budgetMonthRecordArrayList, BudgetMonthRecordClickListener budgetMonthRecordClickListener) {
        this.budgetMonthRecordArrayList = budgetMonthRecordArrayList;
        this.budgetMonthRecordClickListener = budgetMonthRecordClickListener;

    }

    @NonNull
    @Override
    public BudgetMonthRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monthly_expenditure_recycle_view, parent, false);

        // Create View Holder
        final BudgetMonthRecordViewHolder myViewHolder = new BudgetMonthRecordViewHolder(view);

        // Item Clicks
        myViewHolder.itemView.setOnClickListener(v -> budgetMonthRecordClickListener.onItemClicked(budgetMonthRecordArrayList.get(myViewHolder.getLayoutPosition())));


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetMonthRecordViewHolder holder, int position) {
        // Set Month Data

        holder.monthData.setText(String.format("%s %d", new DateFormatSymbols().getMonths()[budgetMonthRecordArrayList.get(position).getMonthNumber()-1], budgetMonthRecordArrayList.get(position).getYearNumber()));


        // Set Month Expenditure
        holder.monthExpenditure.setText(String.format("-$%.2f", budgetMonthRecordArrayList.get(position).getAmountSpentInMonth()));

    }

    @Override
    public int getItemCount() {
        return budgetMonthRecordArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // RecyclerView View Holder
    class BudgetMonthRecordViewHolder extends RecyclerView.ViewHolder {
        private TextView monthData;
        private TextView monthExpenditure;

        BudgetMonthRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            monthData = itemView.findViewById(R.id.monthData);
            monthExpenditure = itemView.findViewById(R.id.monthlyExpenditure);
        }
    }

    // RecyclerView Click Listener
    public interface BudgetMonthRecordClickListener {
        void onItemClicked(BudgetMonthRecord item);
    }
}