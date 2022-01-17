package com.example.pocketcontrol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DatabaseHandler  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PocketControl";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_ITEMS = "items";

    // database key names
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_CATEGORY_NAME = "category_name";

    private static final String KEY_ITEM_ID = "item_id";
    private static final String KEY_FK_CATEGORY_ID = "fk_category_id";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_ITEM_VALUE = "item_value";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + " (" + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY, " + KEY_CATEGORY_NAME + " TEXT NOT NULL);";

        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + " (" + KEY_ITEM_ID + " INTEGER PRIMARY KEY, " + KEY_FK_CATEGORY_ID + " INTEGER NOT NULL, " + KEY_ITEM_NAME + " TEXT NOT NULL, " + KEY_ITEM_VALUE + " NUMERIC NOT NULL, FOREIGN KEY (" + KEY_FK_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + " ON DELETE NO ACTION ON UPDATE NO ACTION);";

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

    void addItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_FK_CATEGORY_ID, item.getFkCategoryID());
        values.put(KEY_ITEM_VALUE, item.getItemValue());

        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    Item getItem(int itemID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ITEMS, new String[] { "*" }, KEY_ITEM_ID + "= ?",
                new String[] { String.valueOf(itemID) }, null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        Item item = new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), Double.parseDouble(cursor.getString(3)));

        db.close();
        return item;
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> itemList = new ArrayList<Item>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all the rows and add to the array list
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), cursor.getString(2), Double.parseDouble(cursor.getString(3)));

                itemList.add(item);
            } while (cursor.moveToNext());
        }

        db.close();
        return itemList;
    }

    public void wipeAllItems() {
        String deleteQuery = "DELETE FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

    void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, category.getCategoryName());

        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categoryList = new ArrayList<Category>();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // loop through all the rows and add to the category list
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        db.close();
        return categoryList;
    }

    public void wipeAllCategories() {
        String deleteQuery = "DELETE FROM " + TABLE_CATEGORIES;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.close();
    }

}
