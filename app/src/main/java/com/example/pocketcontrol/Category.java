package com.example.pocketcontrol;

public class Category {
    private int categoryID;
    private String categoryName;
    private double maxValueToSpendInCategory;
    private double totalValueSpentInCategory;

    public Category(int categoryID, String categoryName, double maxValueToSpendInCategory, double totalValueSpentInCategory) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.maxValueToSpendInCategory = maxValueToSpendInCategory;
        this.totalValueSpentInCategory = totalValueSpentInCategory;
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

    public double getTotalValueSpentInCategory() {
        return this.totalValueSpentInCategory;
    }
}
