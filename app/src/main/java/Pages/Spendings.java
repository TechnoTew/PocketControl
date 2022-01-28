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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.Item;
import com.example.pocketcontrol.ItemArrayAdapter;
import com.example.pocketcontrol.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Spendings extends Fragment {

    DatabaseHandler db;
    RecyclerView itemRecyclerView;
    EditText itemNameEditText;
    TextView itemNameErrorText;
    Spinner categorySpinner;
    EditText itemValueEditText;
    TextView itemValueErrorText;

    public Spendings() {
        // Required empty public constructor
    }

    private void reloadPage() {
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim) // set animation between page transition
                .replace(R.id.homeFragment, new Spendings())
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.fragment_spendings, container, false);

        System.out.println(this.getClass());
        
        // Initialize database handler
        db = new DatabaseHandler(this.getContext());

        // generate the recycler view for android
        generateUIForRecycleView(returnView, db.getAllItems());

        FloatingActionButton addItemButton = returnView.findViewById(R.id.addItemFloatingButton);

        addItemButton.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(returnView.getContext());

            LayoutInflater layoutInflater = getLayoutInflater();

            dialogBuilder.setNegativeButton("Cancel", (dialog, id) -> {
                // User clicked Cancel button
            });

            dialogBuilder.setPositiveButton("Ok", (dialog, id) -> {
                // User clicked OK button
            });

            dialogBuilder.setTitle("Add Item");

            AlertDialog alertDialog = dialogBuilder.create();

            View alertView = layoutInflater.inflate(R.layout.add_item_dialog, null);

            // initialize inputs
            itemNameEditText = (EditText) alertView.findViewById(R.id.itemNameEditText);
            itemNameErrorText = (TextView) alertView.findViewById(R.id.itemNameError);
            categorySpinner = (Spinner) alertView.findViewById(R.id.itemCategorySpinner);
            itemValueEditText = (EditText) alertView.findViewById(R.id.itemValueEditText);
            itemValueErrorText = (TextView) alertView.findViewById(R.id.itemValueError);

            // initialize the spinner with the categories
            ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<Category>(returnView.getContext(), R.layout.simple_spinner_item, db.getAllCategoriesWithItemTotals());
            categoryArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_item);
            categorySpinner.setAdapter(categoryArrayAdapter);

            // set the dialog to have the custom xml
            alertDialog.setView(alertView);

            alertDialog.show();

            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // override the modal from closing
                    final String itemName = itemNameEditText.getText().toString();
                    final int itemCategoryID = ((Category) categorySpinner.getSelectedItem()).getCategoryID();
                    final String itemValueString = itemValueEditText.getText().toString();

                    // check that the item name cannot be empty
                    if (itemName.isEmpty()) {
                        itemNameErrorText.setText("Item Name cannot be empty!");
                        return;
                    }

                    // check that the item value cannot be empty
                    if (itemValueString.isEmpty()) {
                        itemValueErrorText.setText("Item Value cannot be empty!");
                        return;
                    }

                    final double itemValue = itemValueString.isEmpty() ? -1 : (double) Math.round(Double.parseDouble(itemValueString) * 100) / 100;

                    try {
                        db.addItem(new Item(itemCategoryID, itemName, itemValue));
                    } catch (Exception e) {
                        itemNameErrorText.setText("Item Name already exists!");
                        return;
                    }

                    alertDialog.dismiss();

                   reloadPage();
                }
            });
        });

        return returnView;
    }

    public void generateUIForRecycleView(View view, ArrayList<Item> itemArrayList) {
        // Reference of recyclerView
        itemRecyclerView = view.findViewById(R.id.spendingsRecyclerView);

        // Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);

        // Set Layout Manager to RecyclerView
        itemRecyclerView.setLayoutManager(linearLayoutManager);

        // Create adapter
        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(itemArrayList, item -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());

            dialogBuilder.setNegativeButton("Delete", (dialog, id) -> {
                // User clicked Cancel button

                db.deleteItem(item.getItemID());
                reloadPage();
            });

            dialogBuilder.setPositiveButton("Save", (dialog, id) -> {
                // User clicked OK button
            });

            dialogBuilder.setTitle(item.getItemName());

            AlertDialog alertDialog = dialogBuilder.create();

            alertDialog.show();
        });

        // set adapter to RecyclerView
        itemRecyclerView.setAdapter(itemArrayAdapter);
    }
}