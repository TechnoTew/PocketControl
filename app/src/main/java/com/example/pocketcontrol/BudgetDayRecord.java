package com.example.pocketcontrol;

import java.sql.Timestamp;

public class BudgetDayRecord {

    private double amountSpentInDay;
    private int yearNumber;
    private int monthNumber;
    private int dayNumber;

    public BudgetDayRecord(double amountSpentInDay, int dayNumber, int monthNumber, int yearNumber) {
        this.amountSpentInDay = amountSpentInDay;
        this.dayNumber = dayNumber;
        this.monthNumber = monthNumber;
        this.yearNumber = yearNumber;
    }

    public double getAmountSpentInDay() {
        return amountSpentInDay;
    }

    public int getYearNumber() {
        return yearNumber;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    @Override
    public String toString() {
        return String.format("Amount Spent: $%.2f\nDay: %s\nMonth: %s\nYear: %s");
    }

}
