package com.example.pocketcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PocketControl";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_ITEMS = "items";

    // database key names
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_MAX_AMOUNT_TO_SPEND_IN_CATEGORY = "max_amount_to_spend";

    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_FK_CATEGORY_ID = "fk_category_id";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_VALUE = "item_value";
    private static final String KEY_ITEM_TIMESTAMP = "item_created_at";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CATEGORIES_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL UNIQUE, %s NUMERIC NOT NULL);", TABLE_CATEGORIES, KEY_CATEGORY_ID, KEY_CATEGORY_NAME, KEY_MAX_AMOUNT_TO_SPEND_IN_CATEGORY);

        String CREATE_ITEMS_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s INTEGER NOT NULL, %s TEXT NOT NULL, %s NUMERIC NOT NULL, %s NUMERIC DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(%s) REFERENCES %s ON DELETE NO ACTION ON UPDATE NO ACTION);", TABLE_ITEMS, KEY_ITEM_ID, KEY_FK_CATEGORY_ID, KEY_ITEM_NAME, KEY_ITEM_VALUE, KEY_ITEM_TIMESTAMP, KEY_FK_CATEGORY_ID, TABLE_CATEGORIES);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        // Create tables again
        onCreate(db);
    }

    public void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_FK_CATEGORY_ID, item.getFkCategoryID());
        values.put(KEY_ITEM_VALUE, item.getItemValue());

        // if no provided timestamp just set it at the current time
        if (item.getItemTimestamp() != null) {
            values.put(KEY_ITEM_TIMESTAMP, item.getItemTimestamp().toString());
        }

        db.insertOrThrow(TABLE_ITEMS, null, values);
        db.close();
    }

    public void deleteItem(int itemID) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TABLE_ITEMS, String.format("%s = ?", KEY_ITEM_ID), new String[]{String.valueOf(itemID)});
        db.close();
    }

    public void editItem(int itemID, Item newItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, newItem.getItemName());
        values.put(KEY_FK_CATEGORY_ID, newItem.getFkCategoryID());
        values.put(KEY_ITEM_VALUE, newItem.getItemValue());

        db.updateWithOnConflict(TABLE_ITEMS, values, String.format("%s = ?", KEY_ITEM_ID), new String[]{String.valueOf(itemID)}, SQLiteDatabase.CONFLICT_ROLLBACK);
    }

    public ArrayList<Item> getAllItems(Boolean reverseSort, Boolean getThisMonthOnly) {
        ArrayList<Item> itemList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(String.format("SElECT %s, %s, c.%s, %s, %s, %s, CAST(strftime('%%m', %s) AS INTEGER) AS monthNumber FROM %s i INNER JOIN %s c ON c.%s = i.%s %s %s", KEY_ITEM_ID, KEY_ITEM_NAME, KEY_CATEGORY_NAME, KEY_ITEM_VALUE, KEY_FK_CATEGORY_ID, KEY_ITEM_TIMESTAMP, KEY_ITEM_TIMESTAMP, TABLE_ITEMS, TABLE_CATEGORIES, KEY_CATEGORY_ID, KEY_FK_CATEGORY_ID, getThisMonthOnly ? String.format(" WHERE monthNumber = %d ", Calendar.MONTH) : "", reverseSort ? " ORDER BY item_id DESC" : ""), null);

        // loop through all the rows and add to the array list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(4)), cursor.getString(2), cursor.getString(1), Double.parseDouble(cursor.getString(3)), Timestamp.valueOf(cursor.getString(5)));

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itemList;
    }

    public void wipeAllItems() {
        String deleteQuery = "DELETE FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category.getCategoryName());
        values.put(KEY_MAX_AMOUNT_TO_SPEND_IN_CATEGORY, category.getMaxValueToSpendInCategory());

        db.insertOrThrow(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public void deleteCategory(int categoryID) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(TABLE_CATEGORIES, String.format("%s = ?", KEY_CATEGORY_ID), new String[]{String.valueOf(categoryID)});
        db.close();
    }

    public void editCategory(int categoryID, Category newCategory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, newCategory.getCategoryName());
        values.put(KEY_MAX_AMOUNT_TO_SPEND_IN_CATEGORY, newCategory.getMaxValueToSpendInCategory());

        db.updateWithOnConflict(TABLE_CATEGORIES, values, String.format("%s = ?", KEY_CATEGORY_ID), new String[]{String.valueOf(categoryID)}, SQLiteDatabase.CONFLICT_ROLLBACK);
    }

    public ArrayList<Category> getAllCategoriesWithItemTotals(Boolean getThisMonthOnly) {
        ArrayList<Category> categoryList = new ArrayList<>();

        String selectQuery = String.format("SELECT %s, %s, %s, IFNULL(SUM(%s), 0) as total, CAST(strftime('%%m', i.%s) AS INTEGER) AS monthNumber FROM %s c LEFT JOIN %s i ON i.%s = c.%s %s GROUP BY c.%s;", KEY_CATEGORY_ID, KEY_CATEGORY_NAME, KEY_MAX_AMOUNT_TO_SPEND_IN_CATEGORY, KEY_ITEM_VALUE, KEY_ITEM_TIMESTAMP, TABLE_CATEGORIES, TABLE_ITEMS, KEY_FK_CATEGORY_ID, KEY_CATEGORY_ID, getThisMonthOnly ? String.format(" WHERE monthNumber = %d ", Calendar.MONTH) : "", KEY_CATEGORY_ID);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all the rows and add to the category list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(cursor.getInt(0), cursor.getString(1), Double.parseDouble(cursor.getString(2)), Double.parseDouble(cursor.getString(3)));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categoryList;
    }

    public ArrayList<BudgetMonthRecord> getAmountSpentPerMonth() {
        ArrayList<BudgetMonthRecord> budgetMonthRecords = new ArrayList<>();

        String selectQuery = String.format("SELECT SUM(%s), CAST(strftime('%%m', %s) AS INTEGER) AS monthNumber, CAST(strftime('%%Y', %s) AS INTEGER) AS yearNumber FROM %s i GROUP BY yearNumber, monthNumber;", KEY_ITEM_VALUE, KEY_ITEM_TIMESTAMP, KEY_ITEM_TIMESTAMP, TABLE_ITEMS);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all the rows and add to the list
        if (cursor.moveToFirst()) {
            do {
                BudgetMonthRecord budgetMonthRecord = new BudgetMonthRecord(Double.parseDouble(cursor.getString(0)), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(1)));

                budgetMonthRecords.add(budgetMonthRecord);
            } while (cursor.moveToNext());
        }

        return budgetMonthRecords;
    }

    public void wipeAllCategories() {
        String deleteQuery = "DELETE FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }
}
