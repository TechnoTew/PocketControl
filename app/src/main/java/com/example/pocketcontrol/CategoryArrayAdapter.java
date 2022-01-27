package com.example.pocketcontrol;

import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryArrayAdapter extends RecyclerView.Adapter<CategoryArrayAdapter.CategoryViewHolder> {

    private ArrayList<Category> categoryArrayList;
    private CategoryClickListener categoryClickListener;

    public CategoryArrayAdapter(ArrayList<Category> categoryArrayList, CategoryClickListener categoryClickListener) {
        this.categoryArrayList = categoryArrayList;
        this.categoryClickListener = categoryClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_records, parent, false);

        // Create View Holder
        final CategoryViewHolder myViewHolder = new CategoryViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryClickListener.onItemClicked(categoryArrayList.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Set Category Name
        holder.categoryName.setText(categoryArrayList.get(position).getCategoryName());

        // Set Progress Bar
        double percentageSpent = categoryArrayList.get(position).getTotalValueSpentInCategory() / categoryArrayList.get(position).getMaxValueToSpendInCategory() * 100;
        holder.progressBar.setProgress((int) Math.round(percentageSpent), true);

        // Set Progress Bar Colour
        Drawable progressDrawable = holder.progressBar.getProgressDrawable().mutate();
        if (percentageSpent < 50) {
            progressDrawable.setColorFilter(new BlendModeColorFilter(Color.GREEN, BlendMode.SRC_ATOP));
        } else if (percentageSpent < 70) {
            progressDrawable.setColorFilter(new BlendModeColorFilter(Color.YELLOW, BlendMode.SRC_ATOP));
        } else {
            progressDrawable.setColorFilter(new BlendModeColorFilter(Color.RED, BlendMode.SRC_ATOP));
        }

        holder.progressBar.setProgressDrawable(progressDrawable);

        // Set Current Amount spent
        holder.amountSpentInCategory.setText(String.format("$%.2f", categoryArrayList.get(position).getTotalValueSpentInCategory()));

        // Set Max Amount Spent
        holder.totalAmountAllocatedForCategory.setText(String.format("$%.2f", categoryArrayList.get(position).getMaxValueToSpendInCategory()));
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
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
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private ProgressBar progressBar;
        private TextView amountSpentInCategory;
        private TextView totalAmountAllocatedForCategory;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            progressBar = itemView.findViewById(R.id.progressBar);
            amountSpentInCategory = itemView.findViewById(R.id.amountSpentInCategory);
            totalAmountAllocatedForCategory = itemView.findViewById(R.id.totalAmountAllocatedForCategory);
        }
    }

    // RecyclerView Click Listener
    public interface CategoryClickListener {
        void onItemClicked(Category item);
    }
}