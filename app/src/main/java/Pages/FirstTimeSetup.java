package Pages;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.pocketcontrol.AdHandler;
import com.example.pocketcontrol.BudgetMonthRecord;
import com.example.pocketcontrol.Category;
import com.example.pocketcontrol.DatabaseHandler;
import com.example.pocketcontrol.Item;
import com.example.pocketcontrol.R;
import com.example.pocketcontrol.SharedPreferenceHandler;

import java.sql.Timestamp;
import java.util.ArrayList;

public class FirstTimeSetup extends AppCompatActivity {

    private EditText nameField;
    private DatabaseHandler db;
    private SharedPreferenceHandler sph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_setup);

        // roll a chance from 1 to 10 to show an ad
        AdHandler adHandler = new AdHandler(this);
        adHandler.showAdAtRandom(10);

        // uncomment should u need to rebuild whole database
        // getApplicationContext().deleteDatabase("PocketControl");

        // setup shared preference and database handler
        db = new DatabaseHandler(this);

        // uncomment should u need to replace all the categories with default
        // db.wipeAllCategories();

        // check if there is information in the database, if there is not, put in some default data
        if (db.getAllCategoriesWithItemTotals(false).size() == 0) {
            db.addCategory(new Category("Food", 100.00));
            db.addCategory(new Category("Essentials", 50));
            db.addCategory(new Category("Entertainment", 100.00));
            db.addCategory(new Category("Snacks", 50.00));
            db.addCategory(new Category("Transportation", 90.00));
            db.addCategory(new Category("Friends", 150.00));
            db.addCategory(new Category("Miscellaneous", 60.00));
            db.addCategory(new Category("Emergency", 100.00));
        }

        // uncomment should u need to replace all the items with default
        // db.wipeAllItems();

        // new Timestamp(System.currentTimeMillis())
        if (db.getAllItems(false, false).size() == 0) {
            db.addItem(new Item(1, "Roasted Pork & Char Siew Rice", 6.50, Timestamp.valueOf("2021-09-01 08:06:48")));
            db.addItem(new Item(4, "Blueberry Cupcake", 2.50, Timestamp.valueOf("2021-09-05 14:06:48")));
            db.addItem(new Item(3, "Mouse Mat", 10.50, Timestamp.valueOf("2021-10-05 12:06:48")));
            db.addItem(new Item(4, "Strawberry Cupcake", 2.50, Timestamp.valueOf("2021-10-05 16:16:48")));
            db.addItem(new Item(1, "Chicken Rice", 3.50, Timestamp.valueOf("2021-11-05 14:06:48")));
            db.addItem(new Item(1, "Cheese Chicken Chop", 5.50, Timestamp.valueOf("2021-11-05 14:06:48")));
            db.addItem(new Item(4, "Chocolate Ice Cream", 1.50, Timestamp.valueOf("2021-11-05 14:36:48")));
            db.addItem(new Item(5, "Ez-Link Card Topup", 20.00, Timestamp.valueOf("2021-12-08 12:37:51")));
            db.addItem(new Item(2, "Monthly Mobile Plan Subscription", 13.00, Timestamp.valueOf("2021-12-08 12:36:48")));
            db.addItem(new Item(2, "Kopitiam Card Topup", 20.00, Timestamp.valueOf("2021-01-15 12:37:51")));
            db.addItem(new Item(1, "Roasted Pork Rice", 13.00, Timestamp.valueOf("2021-01-17 15:53:48")));
            db.addItem(new Item(5, "Grab to School", 20.0, Timestamp.valueOf("2021-01-17 8:30:48")));
            db.addItem(new Item(3, "Logitech G304 Mouse", 79.90, Timestamp.valueOf("2022-02-01 8:30:48")));
            db.addItem(new Item(5, "Grab to School", 20.0, Timestamp.valueOf("2022-02-02 8:30:48")));
            db.addItem(new Item(6, "Meal and Bowling with Brandon", 45.00, Timestamp.valueOf("2022-02-02 8:30:48")));
            db.addItem(new Item(7, "Gift for Mom", 30.00, Timestamp.valueOf("2022-02-03 8:30:48")));
            db.addItem(new Item(8, "Doctor Visit for Fever", 60.00, Timestamp.valueOf("2022-02-03 8:30:48")));
        }

        ArrayList<Category> categoriesWithItemTotals = db.getAllCategoriesWithItemTotals(false);
        Log.d("", "All Categories with item totals: ");
        for (Category category : categoriesWithItemTotals) {
            Log.d("", String.format("%s: $%.2f", category.getCategoryName(), category.getTotalValueSpentInCategory()));
        }

        ArrayList<Item> items = db.getAllItems(false, false);
        for (Item item : items) {
            Log.d("", String.format("%s (%s): $%.2f", item.getItemName(), item.getItemCategoryName()
                    , item.getItemValue()));
        }

        ArrayList<BudgetMonthRecord> budgetMonthRecords = db.getAmountSpentPerMonth(false);
        for (BudgetMonthRecord budgetMonthRecord : budgetMonthRecords) {
            Log.d("", budgetMonthRecord.debugInfo());
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