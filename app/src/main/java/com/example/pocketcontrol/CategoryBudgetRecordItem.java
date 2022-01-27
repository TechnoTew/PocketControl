package com.example.pocketcontrol;

public class CategoryBudgetRecordItem {
    private String categoryName;
    private double amountSpentInCategory;
    private double maxAmountInCategory;

    public CategoryBudgetRecordItem(String categoryName, double amountSpentInCategory, double maxAmountInCategory) {
        this.categoryName = categoryName;
        this.amountSpentInCategory = amountSpentInCategory;
        this.maxAmountInCategory = maxAmountInCategory;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getAmountSpentInCategory() {
        return amountSpentInCategory;
    }

    public double getMaxAmountInCategory() {
        return maxAmountInCategory;
    }
}
