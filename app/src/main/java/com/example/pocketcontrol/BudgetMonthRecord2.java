package com.example.pocketcontrol;

public class BudgetMonthRecord2 {
    private double amountSpentInMonth;
    private int yearNumber;
    private int monthNumber;

    public BudgetMonthRecord2(double amountSpentInMonth, int yearNumber, int monthNumber) {
        this.amountSpentInMonth = amountSpentInMonth;
        this.yearNumber = yearNumber;
        this.monthNumber = monthNumber;
    }

    public double getAmountSpentInMonth() {
        return amountSpentInMonth;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public String debugInfo() {
        return String.format("Amount spend in the month: $%.2f\nMonth Number: %d\nYear Number: %d", amountSpentInMonth, monthNumber, yearNumber);
    }
}
