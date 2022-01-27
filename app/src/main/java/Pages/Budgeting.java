package Pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.CategoryArrayAdapter;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.R;

import java.util.ArrayList;

public class Budgeting extends Fragment {

    DatabaseHandler db;
    RecyclerView categoryBudgetRecordItemView;

    public Budgeting() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_budgeting, container, false);

        db = new DatabaseHandler(this.getContext());
        ArrayList<Category> categories = db.getAllCategoriesWithItemTotals();
        generateUIForRecycleView(returnView, categories);

        return returnView;
    }

    public View generateUIForRecycleView(View view, ArrayList<Category> categoryArrayList) {
        // Reference of RecyclerView
        categoryBudgetRecordItemView = view.findViewById(R.id.budgetView);

        // Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);

        // Set Layout Manager to RecyclerView
        categoryBudgetRecordItemView.setLayoutManager(linearLayoutManager);

        // Create adapter
        CategoryArrayAdapter myRecyclerViewAdapter = new CategoryArrayAdapter(categoryArrayList, new CategoryArrayAdapter.CategoryClickListener() {
            @Override
            public void onItemClicked(Category item) {

            }
        });

        // set adapter to RecyclerView
        categoryBudgetRecordItemView.setAdapter(myRecyclerViewAdapter);

        return view;
    }
}