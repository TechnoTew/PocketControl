package com.example.pocketcontrol;

public class LastSpendingRecordItem {
    private String itemName;
    private Double value;

    public LastSpendingRecordItem(String itemName, Double value) {
        this.itemName = itemName;
        this.value = value;
    }

    public String getItemName() {
        return this.itemName;
    }

    public Double getValue() {
        return this.value;
    }
}
