package com.TelegramBot.db;

import lombok.Getter;

@Getter
public class ShoppingMgmt {
    private String product;
    private String price;
    private String company;
    private String note;
    private String purchaseDate;
    private int index;


    public ShoppingMgmt(){}

    public ShoppingMgmt(String price) {
        this.price = price;
    }



    public ShoppingMgmt(String product, String price, String company, String note, String purchaseDate) {
        this.product = product;
        this.price = price;
        this.company = company;
        this.note = note;
        this.purchaseDate = purchaseDate;
    }

    public ShoppingMgmt(String product, String price, String company, String note, String purchaseDate, int index) {
        this.product = product;
        this.price = price;
        this.company = company;
        this.note = note;
        this.purchaseDate = purchaseDate;
        this.index = index;
    }

    @Override
    public String toString() {
        return
                "Product: " + getProduct() +
                "\nPrice: " + getPrice() +
                "\nCompany: " + getCompany() +
                "\nNote: " + getNote() +
                "\nPurchase Date: " + getPurchaseDate()
                        + "\n-----------\n";
    }



}
