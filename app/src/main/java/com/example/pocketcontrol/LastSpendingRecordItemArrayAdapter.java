package com.example.pocketcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LastSpendingRecordItemArrayAdapter extends RecyclerView.Adapter<LastSpendingRecordItemArrayAdapter.MyViewHolder> {

    private ArrayList<LastSpendingRecordItem> lastSpendingRecordItemArrayList;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public LastSpendingRecordItemArrayAdapter(ArrayList<LastSpendingRecordItem> lastSpendingRecordItemArrayList, MyRecyclerViewItemClickListener itemClickListener) {
        this.lastSpendingRecordItemArrayList = lastSpendingRecordItemArrayList;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_records, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(lastSpendingRecordItemArrayList.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Set Category Name
        holder.textViewCategoryName.setText(lastSpendingRecordItemArrayList.get(position).getItemName());

        //Set Currency
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

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCategoryName;
        ;
        private TextView textViewValue;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.itemName);
            textViewValue = itemView.findViewById(R.id.value);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(LastSpendingRecordItem item);
    }
}