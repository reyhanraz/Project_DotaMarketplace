package com.project.dotamarketplace.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    int transactionID, userID, itemID;
    int quantity;
    String transactionDate;

    public Transaction(){

    }

    public Transaction(int transactionID, int userID, int itemID, int quantity) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.itemID = itemID;
        this.quantity = quantity;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        this.transactionDate = dateFormat.format(date);
    }

    public Transaction(int transactionID, int userID, int itemID, int quantity, String transactionDate) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.itemID = itemID;
        this.quantity = quantity;
        this.transactionDate = transactionDate;
    }

    public long getTransactionID() {
        return transactionID;
    }

    public long getUserID() {
        return userID;
    }

    public long getItemID() {
        return itemID;
    }

    public int getQuantity() {
        return quantity;
    }


    public String getTransactionDate() {
        return transactionDate;
    }
}
