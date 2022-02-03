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

public class CategoryDetailsArrayAdapter extends RecyclerView.Adapter<CategoryDetailsArrayAdapter.CategoryViewHolder> {

    private ArrayList<Category> categoryArrayList;
    private CategoryClickListener categoryClickListener;
    int okayColour, warningColour, dangerColour;

    public CategoryDetailsArrayAdapter(ArrayList<Category> categoryArrayList, CategoryClickListener categoryClickListener) {
        this.categoryArrayList = categoryArrayList;
        this.categoryClickListener = categoryClickListener;

    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_recycle_view, parent, false);

        // Create View Holder
        final CategoryViewHolder myViewHolder = new CategoryViewHolder(view);

        // Item Clicks
        myViewHolder.itemView.setOnClickListener(v -> categoryClickListener.onItemClicked(categoryArrayList.get(myViewHolder.getLayoutPosition())));

        if (ThemeManager.isDarkTheme(view)) {
            // is dark theme
            okayColour = ContextCompat.getColor(view.getContext(), R.color.VeryLightGreen);
            warningColour = ContextCompat.getColor(view.getContext(), R.color.Yellow);
            dangerColour = ContextCompat.getColor(view.getContext(), R.color.PalePink);
        } else {
            // is light theme or default
            okayColour = ContextCompat.getColor(view.getContext(), R.color.Blue);
            warningColour = ContextCompat.getColor(view.getContext(), R.color.VeryDarkOrange);
            dangerColour = ContextCompat.getColor(view.getContext(), R.color.DarkerRed);
        }



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
            progressDrawable.setColorFilter(new BlendModeColorFilter(okayColour, BlendMode.SRC_ATOP));
        } else if (percentageSpent < 70) {
            progressDrawable.setColorFilter(new BlendModeColorFilter(warningColour, BlendMode.SRC_ATOP));
        } else {
            progressDrawable.setColorFilter(new BlendModeColorFilter(dangerColour, BlendMode.SRC_ATOP));
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