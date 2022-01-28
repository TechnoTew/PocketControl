package Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.Item;
import com.example.pocketcontrol.R;
import com.example.pocketcontrol.SharedPreferenceHandler;

import java.util.ArrayList;

public class FirstTimeSetup extends AppCompatActivity {

    EditText nameField;
    DatabaseHandler db;
    SharedPreferenceHandler sph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_setup);

        // uncomment should u need to rebuild whole database
        // getApplicationContext().deleteDatabase("PocketControl");

        // setup shared preference and database handler
        db = new DatabaseHandler(this);

        // uncomment should u need to replace all the categories with default
        // db.wipeAllCategories();

        // check if there is information in the database, if there is not, put in some default data
        if (db.getAllCategoriesWithItemTotals().size() == 0) {
            db.addCategory(new Category("Food", 100.00));
            db.addCategory(new Category("Essentials", 50));
            db.addCategory(new Category("Entertainment", 100.00));
            db.addCategory(new Category("Snacks", 50.00));
        }

        // uncomment should u need to replace all the items with default
        // db.wipeAllItems();

        if (db.getAllItems(false).size() == 0) {
            db.addItem(new Item(1, "Chicken Rice", 3.50));
            db.addItem(new Item(1, "Cheese Chicken Chop", 3.50));
            db.addItem(new Item(2, "Ez-Link Card Topup", 20.00));
            db.addItem(new Item(2, "Monthly Mobile Plan Subscription", 13.00));
            db.addItem(new Item(3, "Logitech G304 Mouse", 79.90));
            db.addItem(new Item(4, "Waffle", 1.00));
        }

        ArrayList<Category> categoriesWithItemTotals = db.getAllCategoriesWithItemTotals();
        Log.d("", "All Categories with item totals: ");
        for (Category category : categoriesWithItemTotals) {
            Log.d("", String.format("%s: $%.2f", category.getCategoryName(), category.getTotalValueSpentInCategory()));
        }

        ArrayList<Item> items = db.getAllItems(false);
        for (Item item : items) {
            Log.d("", String.format("%s (%s): $%.2f", item.getItemName(), item.getItemCategoryName()
                    , item.getItemValue()));
        }

        sph = new SharedPreferenceHandler(getApplicationContext());
        // Use this whenever you wish to reset the first setup process
        // sph.setSetupStatus(false);

        Boolean setupStatus = sph.getSetupStatus();

        if (setupStatus) {
            // log the details for reference
            Log.d("User Name", sph.getUserName());
            Log.d("User Theme", sph.getTheme());

            // set theme of application

            // already setup, redirect to main page
            Intent i = new Intent(this, NavActivity.class);
            startActivity(i);

            // kill off the activity so the user cannot return to it
            finish();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.resetButton:
                nameField = (EditText) findViewById(R.id.nameEditBox);
                nameField.setText("");
                break;
            case R.id.doneButton:
                nameField = (EditText) findViewById(R.id.nameEditBox);
                String userName = nameField.getText().toString();
                if (userName.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Name Input Box cannot be empty!").setMessage("You must input a name!");


                    builder.setPositiveButton("Ok", (dialog, id) -> {
                        // User clicked OK button
                    });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                    break;
                }

                sph.setUserName(nameField.getText().toString());
                sph.setSetupStatus(true);
                Intent i = new Intent(this, NavActivity.class);
                startActivity(i);

                // kill off the activity so the user cannot return to it
                finish();
        }
    }
}