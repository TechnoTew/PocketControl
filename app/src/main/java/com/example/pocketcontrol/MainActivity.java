package com.example.pocketcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText nameField;
    DatabaseHandler db;
    SharedPreferenceHandler sph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup shared preference and database handler
        db = new DatabaseHandler(this);


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
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);

            // kill off the activity so the user cannot return to it
            finish();
        }

        // uncomment should u need to replace all the categories with default
        // db.wipeAllCategories();

        // check if there is information in the database, if there is not, put in some default data
        if (db.getAllCategories().size() == 0) {
            db.addCategory(new Category("Food"));
            db.addCategory(new Category("Misc"));
            db.addCategory(new Category("Entertainment"));
        }

        // uncomment should u need to replace all the items with default
        //db.wipeAllItems();

        if (db.getAllItems().size() == 0) {
            db.addItem(new Item(1, "Chicken Rice", 3.50));
            db.addItem(new Item(1, "Waffle", 1.00));
            db.addItem(new Item(3, "Logitech G304 Mouse", 79.90));
            db.addItem(new Item(2, "Ez-Link Card Topup", 10.00));
        }

        ArrayList<Category> categoriesWithItemTotals = db.getAllCategoriesWithItemTotals();
        Log.d("", "All Categories with item totals: ");
        for (Category category : categoriesWithItemTotals) {
            Log.d("", String.format("%s: $%.2f", category.getCategoryName(), category.getTotalValueInCategory()));
        }

        ArrayList<Item> items = db.getAllItems();
        for (Item item : items) {
            Log.d("", String.format("%s (%s): $%.2f", item.getItemName(), item.getItemCategoryName()
                    , item.getItemValue()));
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
                    Toast.makeText(getApplicationContext(), "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                    break;
                }

                sph.setUserName(nameField.getText().toString());
                sph.setSetupStatus(true);
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);

                // kill off the activity so the user cannot return to it
                finish();
        }
    }
}