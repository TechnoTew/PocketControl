package com.example.pocketcontrol;

public class Category {
    private int categoryID;
    private String categoryName;
    private double totalValueInCategory;


    public Category(int categoryID, String categoryName, double totalValueInCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.totalValueInCategory = totalValueInCategory;
    }

    public Category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getTotalValueInCategory() {
        return totalValueInCategory;
    }
}
