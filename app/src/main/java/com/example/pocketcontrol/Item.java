package com.example.pocketcontrol;

import java.sql.Timestamp;

public class Item {

    private int itemID;
    private int fkCategoryID;
    private String itemCategoryName;
    private String itemName;
    private double itemValue;
    private Timestamp itemTimestamp;

    // constructor
    public Item(int id, int categoryID, String categoryName, String name, double value, Timestamp timestamp) {
        this.itemID = id;
        this.fkCategoryID = categoryID;
        this.itemCategoryName = categoryName;
        this.itemName = name;
        this.itemValue = value;
        this.itemTimestamp = timestamp;
    }

    public Timestamp getItemTimestamp() {
        return itemTimestamp;
    }

    public Item(int categoryID, String name, double value, Timestamp timestamp) {
        this.fkCategoryID = categoryID;
        this.itemName = name;
        this.itemValue = value;
        this.itemTimestamp = timestamp;
    }

    public Item(int categoryID, String name, double value) {
        this.fkCategoryID = categoryID;
        this.itemName = name;
        this.itemValue = value;
    }

    public void setFkCategoryID(int fkCategoryID) {
        this.fkCategoryID = fkCategoryID;
    }

    public void setItemCategoryName(String itemCategoryName) {
        this.itemCategoryName = itemCategoryName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemValue(double itemValue) {
        this.itemValue = itemValue;
    }

    public String getItemCategoryName() {
        return itemCategoryName;
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

    public String debugItemInfo() {
        return String.format("Item Name: %s\nItem FK_Category ID: %d\nItem Category name: %s\nItem Value: $%.2f\nItem Timestamp: %s", this.itemName, this.fkCategoryID, this.itemCategoryName, this.itemValue, this.itemTimestamp);
    }
}
