package com.example.pocketcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LastSpendingRecordItemArrayAdapter extends RecyclerView.Adapter<LastSpendingRecordItemArrayAdapter.lastSpendingRecordItemViewHolder> {

    private ArrayList<LastSpendingRecordItem> lastSpendingRecordItemArrayList;
    private lastSpendingRecordItemClickListener itemLastSpendingRecordItemClickListener;

    public LastSpendingRecordItemArrayAdapter(ArrayList<LastSpendingRecordItem> lastSpendingRecordItemArrayList, lastSpendingRecordItemClickListener itemLastSpendingRecordItemClickListener) {
        this.lastSpendingRecordItemArrayList = lastSpendingRecordItemArrayList;
        this.itemLastSpendingRecordItemClickListener = itemLastSpendingRecordItemClickListener;
    }

    @NonNull
    @Override
    public lastSpendingRecordItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_records, parent, false);

        // Create View Holder
        final lastSpendingRecordItemViewHolder myViewHolder = new lastSpendingRecordItemViewHolder(view);

        // Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemLastSpendingRecordItemClickListener.onItemClicked(lastSpendingRecordItemArrayList.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull lastSpendingRecordItemViewHolder holder, int position) {
        // Set Category Name
        holder.textViewCategoryName.setText(lastSpendingRecordItemArrayList.get(position).getItemName());

        // Set Value
        String value = String.format("-$%.2f", lastSpendingRecordItemArrayList.get(position).getValue());
        holder.textViewValue.setText(value);

    }

    @Override
    public int getItemCount() {
        return lastSpendingRecordItemArrayList.size();
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
    class lastSpendingRecordItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCategoryName;
        ;
        private TextView textViewValue;

        lastSpendingRecordItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.itemName);
            textViewValue = itemView.findViewById(R.id.value);
        }
    }

    // RecyclerView Click Listener
    public interface lastSpendingRecordItemClickListener {
        void onItemClicked(LastSpendingRecordItem item);
    }
}