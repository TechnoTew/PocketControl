package com.example.pocketcontrol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ItemViewHolder> {

    private ArrayList<Item> itemArrayList;
    private ItemClickListener itemClickListener;

    public ItemArrayAdapter(ArrayList<Item> itemArrayList, ItemClickListener itemClickListener) {
        this.itemArrayList = itemArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_view, parent, false);

        // Create View Holder
        final ItemViewHolder myViewHolder = new ItemViewHolder(view);

        // Item Clicks
        myViewHolder.itemView.setOnClickListener(v -> itemClickListener.onItemClicked(itemArrayList.get(myViewHolder.getLayoutPosition())));

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
      // Set Item Name
        holder.itemName.setText(itemArrayList.get(position).getItemName());

        // Set Item Value
        holder.itemValue.setText(String.format("-$%.2f", itemArrayList.get(position).getItemValue()));
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
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
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemValue;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemValue = itemView.findViewById(R.id.itemValue);
        }
    }

    // RecyclerView Click Listener
    public interface ItemClickListener {
        void onItemClicked(Item item);
    }
}