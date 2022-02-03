package Pages;

import android.app.AlertDialog;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import com.example.pocketcontrol.AdHandler;
import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.CategoryDetailsArrayAdapter;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.R;
import com.example.pocketcontrol.ThemeManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Budgeting extends Fragment {

    private DatabaseHandler db;
    private RecyclerView categoryBudgetRecordItemView;
    private EditText categoryNameEditText;
    private TextView categoryNameErrorText;
    private EditText categoryMaxValueEditText;
    private TextView categoryValueErrorText;

    // variables to initialize the max length/ amount of category details
    private int maxNoOfCharForCategoryName = 14;
    private double minCategoryMaxValue = 1;
    private double maxCategoryMaxValue = 100000;

    private View rootView;

    public Budgeting() {
        super(R.layout.fragment_budgeting);
    }

    private void reloadPage() {
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim) // set animation between page transition
                .replace(R.id.homeFragment, new Budgeting())
                .commit();
    }

    public void onStart() {
        super.onStart();

        rootView = this.getView();

        // roll a chance from 1 to 10 to show an ad
        AdHandler adHandler = new AdHandler(this.getActivity());
        adHandler.showAdAtRandom(10);

        db = new DatabaseHandler(this.getContext());

        ArrayList<Category> categories = db.getAllCategoriesWithItemTotals(false);

        generateUIForRecycleView(this.getView(), categories);

        FloatingActionButton addCategoryButton = this.getView().findViewById(R.id.addBudgetFloatingButton);

        addCategoryButton.setOnClickListener(addBudgetButtonView -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getView().getContext());

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

            // if dark theme, set the text of the buttons to white
            if (ThemeManager.isDarkTheme(rootView)) {
                addCategoryDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white, rootView.getContext().getTheme()));
                addCategoryDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.white, rootView.getContext().getTheme()));
                addCategoryDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white, rootView.getContext().getTheme()));
            }

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

                // check that the category name length is not exceeding (maxNoOfCharForCategoryName) characters
                if (newCategoryName.length() > maxNoOfCharForCategoryName) {
                    categoryNameErrorText.setText("Category Name cannot exceed 14 characters!");
                    return;
                }

                // check that the category value cannot be empty
                if (newCategoryMaxBudgetValueString.isEmpty()) {
                    categoryValueErrorText.setText("Max Value for Category cannot be empty!");
                    return;
                }

                // check that the category max value cannot be below (minCategoryMaxValue), or be above (maxCategoryMaxValue)
                if (Double.compare(newCategoryMaxBudgetValue, minCategoryMaxValue) < 0 || Double.compare(newCategoryMaxBudgetValue, maxCategoryMaxValue) > 0) {
                    categoryValueErrorText.setText(Double.compare(newCategoryMaxBudgetValue, minCategoryMaxValue) < 0 ? String.format("Min Value for Category cannot be under $%.2f", minCategoryMaxValue) : String.format("Max Value for Category cannot be above $%.2f", maxCategoryMaxValue));
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
            categoryMaxValueEditText.setHint(String.format("$%.2f", category.getMaxValueToSpendInCategory()));

            editCategoryDialog.setView(alertView);

            editCategoryDialog.show();

            // if dark theme, set the text of the buttons to white
            if (ThemeManager.isDarkTheme(rootView)) {
                editCategoryDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white, rootView.getContext().getTheme()));
                editCategoryDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.white, rootView.getContext().getTheme()));
                editCategoryDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.white, rootView.getContext().getTheme()));
            }


            editCategoryDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(alertDialogView -> {
                // override the modal from closing
                final String newCategoryName = categoryNameEditText.getText().toString();
                final String newCategoryMaxBudgetValueString = categoryMaxValueEditText.getText().toString();

                final double newCategoryMaxBudgetValue = newCategoryMaxBudgetValueString.isEmpty() ? -1 : (double) Math.round(Double.parseDouble(newCategoryMaxBudgetValueString) * 100) / 100;

                // category object to store the changed category (set the default to the old category details)
                Category updateCategory = new Category(category.getCategoryName(), category.getMaxValueToSpendInCategory());

                // check that the category name length is not exceeding (maxNoOfCharForCategoryName) characters
                if (newCategoryName.length() > maxNoOfCharForCategoryName) {
                    categoryNameErrorText.setText("Category Name cannot exceed 14 characters!");
                    return;
                }

                // check that the category max value cannot be below (minCategoryMaxValue), or be above (maxCategoryMaxValue)
                if (Double.compare(newCategoryMaxBudgetValue, -1) != 0 && Double.compare(newCategoryMaxBudgetValue, minCategoryMaxValue) < 0 || Double.compare(newCategoryMaxBudgetValue, maxCategoryMaxValue) > 0) {
                    categoryValueErrorText.setText(Double.compare(newCategoryMaxBudgetValue, minCategoryMaxValue) < 0 ? String.format("Min Value for Category cannot be under $%.2f", minCategoryMaxValue) : String.format("Max Value for Category cannot be above $%.2f", maxCategoryMaxValue));
                    return;
                }

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