package com.example.pocketcontrol;

public class Category {
    private int categoryID;
    private String categoryName;
    private double maxValueToSpendInCategory;
    private double totalValueInCategory;

    public Category(int categoryID, String categoryName, double maxValueToSpendInCategory, double totalValueInCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.maxValueToSpendInCategory = maxValueToSpendInCategory;
        this.totalValueInCategory = totalValueInCategory;
    }

    public Category(int categoryID, String categoryName, double maxValueToSpendInCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.maxValueToSpendInCategory = maxValueToSpendInCategory;
    }

    public Category(String categoryName, double maxValueToSpendInCategory) {
        this.categoryName = categoryName;
        this.maxValueToSpendInCategory = maxValueToSpendInCategory;
    }

    public int getCategoryID() {
        return this.categoryID;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public double getMaxValueToSpendInCategory() {
        return this.maxValueToSpendInCategory;
    }

    public double getTotalValueInCategory() {
        return this.totalValueInCategory;
    }
}
