package Pages;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.CategoryDetailsArrayAdapter;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Budgeting extends Fragment {

    private DatabaseHandler db;
    private RecyclerView categoryBudgetRecordItemView;
    private EditText categoryNameEditText;
    private TextView categoryNameErrorText;
    private EditText categoryMaxValueEditText;
    private TextView categoryValueErrorText;

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

        FloatingActionButton addCategoryButton = returnView.findViewById(R.id.addBudgetFloatingButton);

        addCategoryButton.setOnClickListener(addBudgetButtonView -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(returnView.getContext());

            LayoutInflater layoutInflater = getLayoutInflater();

            dialogBuilder.setNegativeButton("Cancel", (dialog, id) -> {
                // User clicked Cancel button
            });

            dialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
                // User clicked OK button
            });

            dialogBuilder.setTitle("Add Category");

            AlertDialog addCategoryDialog = dialogBuilder.create();

            View alertView = layoutInflater.inflate(R.layout.category_details_dialog, null);

            // initialize inputs
            categoryNameEditText = alertView.findViewById(R.id.itemNameEditText);
            categoryNameErrorText = alertView.findViewById(R.id.itemNameError);
            categoryMaxValueEditText = alertView.findViewById(R.id.itemValueEditText);
            categoryValueErrorText = alertView.findViewById(R.id.itemValueError);

            addCategoryDialog.setView(alertView);

            addCategoryDialog.show();

            addCategoryDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(alertDialogView -> {
                // override the modal from closing
                final String newCategoryName = categoryNameEditText.getText().toString();
                final String newCategoryMaxBudgetValueString = categoryMaxValueEditText.getText().toString();

                final double newCategoryMaxBudgetValue = newCategoryMaxBudgetValueString.isEmpty() ? -1 : (double) Math.round(Double.parseDouble(newCategoryMaxBudgetValueString) * 100) / 100;

                // check that the category name cannot be empty
                if (newCategoryName.isEmpty()) {
                    categoryNameErrorText.setText("Category Name cannot be empty!");
                    return;
                }

                // check that the category value cannot be empty
                if (newCategoryMaxBudgetValueString.isEmpty()) {
                    categoryValueErrorText.setText("Max Value for Category cannot be empty!");
                    return;
                }

                try {
                    db.addCategory(new Category(newCategoryName, newCategoryMaxBudgetValue));
                } catch (Exception e) {
                    categoryNameErrorText.setText("Category Name already exists!");
                    return;
                }

                addCategoryDialog.dismiss();

                reloadPage();
            });
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
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());

            LayoutInflater layoutInflater = getLayoutInflater();

            dialogBuilder.setNegativeButton("Cancel", (dialog, id) -> {
                // User clicked Cancel button
            });

            dialogBuilder.setNeutralButton("Delete", (dialog, id) -> {
                // User clicked Delete button

                db.deleteCategory(category.getCategoryID());
                reloadPage();
            });

            dialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
                // User clicked OK button
            });

            dialogBuilder.setTitle("Edit Category");

            AlertDialog editCategoryDialog = dialogBuilder.create();

            View alertView = layoutInflater.inflate(R.layout.category_details_dialog, null);

            // initialize inputs
            categoryNameEditText = alertView.findViewById(R.id.itemNameEditText);
            categoryNameErrorText = alertView.findViewById(R.id.itemNameError);
            categoryMaxValueEditText = alertView.findViewById(R.id.itemValueEditText);
            categoryValueErrorText = alertView.findViewById(R.id.itemValueError);

            // initialize fields to hold the previous value
            categoryNameEditText.setHint(category.getCategoryName());
            categoryMaxValueEditText.setHint(String.valueOf(category.getMaxValueToSpendInCategory()));

            editCategoryDialog.setView(alertView);

            editCategoryDialog.show();

            editCategoryDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(alertDialogView -> {
                // override the modal from closing
                final String newCategoryName = categoryNameEditText.getText().toString();
                final String newCategoryMaxBudgetValueString = categoryMaxValueEditText.getText().toString();

                final double newCategoryMaxBudgetValue = newCategoryMaxBudgetValueString.isEmpty() ? -1 : (double) Math.round(Double.parseDouble(newCategoryMaxBudgetValueString) * 100) / 100;

                // category object to store the changed category (set the default to the old category details)
                Category updateCategory = new Category(category.getCategoryName(), category.getMaxValueToSpendInCategory());

                if (!newCategoryName.isEmpty() && !newCategoryName.equals(category.getCategoryName())) updateCategory.setCategoryName(newCategoryName);
                if (!newCategoryMaxBudgetValueString.isEmpty() && newCategoryMaxBudgetValue != category.getMaxValueToSpendInCategory()) updateCategory.setMaxValueToSpendInCategory(newCategoryMaxBudgetValue);

                try {
                    db.editCategory(category.getCategoryID(), updateCategory);
                } catch (Exception e) {
                    categoryNameErrorText.setText("Category Name already exists!");
                    return;
                }

                editCategoryDialog.dismiss();

                reloadPage();
            });
        });

        // set adapter to RecyclerView
        categoryBudgetRecordItemView.setAdapter(budgetArrayAdapter);
    }
}