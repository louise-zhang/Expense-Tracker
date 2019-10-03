package com.example.expensetracker;

import java.io.Serializable;

public class TransactionBean implements Serializable {

    private String amount;
    private String category;
    private String datePick;


    public TransactionBean(String amount, String category, String date, String datePick) {
        this.amount = amount;
        this.category = category;
        this.datePick = datePick;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDatePick() {
        return datePick;
    }

    public void setDatePick(String datePick) {
        this.datePick = datePick;
    }

//    public String getImgUrl() {
//        return imgUrl;
//    }
//
//    public void setImgUrl(String imgUrl) {
//        this.imgUrl = imgUrl;
//    }
}
