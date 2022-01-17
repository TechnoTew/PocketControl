package com.example.pocketcontrol;

public class Item {

    private int itemID;
    private int fkCategoryID;
    private String itemName;
    private double itemValue;

    // constructor
    public Item(int id, int categoryID, String name, double value) {
        this.itemID = id;
        this.fkCategoryID = categoryID;
        this.itemName = name;
        this.itemValue = value;
    }

    public Item(int categoryID, String name, double value) {
        this.fkCategoryID = categoryID;
        this.itemName = name;
        this.itemValue = value;
    }

    public int getItemID() {
        return itemID;
    }

    public int getFkCategoryID() {
        return fkCategoryID;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemValue() {
        return itemValue;
    }
}
