package Pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.CategoryDetailsArrayAdapter;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.Item;
import com.example.pocketcontrol.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Budgeting extends Fragment {

    private DatabaseHandler db;
    private RecyclerView categoryBudgetRecordItemView;
    private EditText itemNameEditText;
    private TextView itemNameErrorText;
    private EditText itemValueEditText;
    private TextView itemValueErrorText;

    public Budgeting() {
        // Required empty public constructor
    }

    private void reloadPage() {
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim) // set animation between page transition
                .replace(R.id.homeFragment, new Budgeting())
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_budgeting, container, false);

        db = new DatabaseHandler(this.getContext());

        ArrayList<Category> categories = db.getAllCategoriesWithItemTotals();

        generateUIForRecycleView(returnView, categories);

        FloatingActionButton addBudgetButton = returnView.findViewById(R.id.addBudgetFloatingButton);

        addBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(returnView.getContext());

                LayoutInflater layoutInflater = getLayoutInflater();

                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked Cancel button
                    }
                });

                dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                });

                dialogBuilder.setTitle("Add Budget");

                AlertDialog alertDialog = dialogBuilder.create();

                View alertView = layoutInflater.inflate(R.layout.add_budget_dialog, null);

                // initialize inputs
                itemNameEditText = (EditText) alertView.findViewById(R.id.itemNameEditText);
                itemNameErrorText = (TextView) alertView.findViewById(R.id.itemNameError);
                itemValueEditText = (EditText) alertView.findViewById(R.id.itemValueEditText);
                itemValueErrorText = (TextView) alertView.findViewById(R.id.itemValueError);

                alertDialog.setView(alertView);

                alertDialog.show();

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // override the modal from closing
                        final String CategoryName = itemNameEditText.getText().toString();
                        final String MaxBudgetValueString = itemValueEditText.getText().toString();

                        // check that the category name cannot be empty
                        if (CategoryName.isEmpty()) {
                            itemNameErrorText.setText("Item Name cannot be empty!");
                            return;
                        }

                        // check that the category value cannot be empty
                        if (MaxBudgetValueString.isEmpty()) {
                            itemValueErrorText.setText("Item Value cannot be empty!");
                            return;
                        }

                        final double maxbudgetValue = MaxBudgetValueString.isEmpty() ? -1 : (double) Math.round(Double.parseDouble(MaxBudgetValueString) * 100) / 100;
                        try {
                            db.addCategory(new Category(CategoryName, maxbudgetValue));
                        } catch (Exception e) {
                            itemNameErrorText.setText("Item Name already exists!");
                            return;
                        }

                        alertDialog.cancel();

                        reloadPage();
                    }
                });
            }
        });

        return returnView;
    }

    public void generateUIForRecycleView(View view, ArrayList<Category> categoryArrayList) {
        // Reference of RecyclerView
        categoryBudgetRecordItemView = view.findViewById(R.id.budgetView);

        // Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);

        // Set Layout Manager to RecyclerView
        categoryBudgetRecordItemView.setLayoutManager(linearLayoutManager);

        // Create adapter
        CategoryDetailsArrayAdapter budgetArrayAdapter = new CategoryDetailsArrayAdapter(categoryArrayList, category -> {

        });

        // set adapter to RecyclerView
        categoryBudgetRecordItemView.setAdapter(budgetArrayAdapter);

    }
}