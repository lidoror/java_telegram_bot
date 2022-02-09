package com.TelegramBot.db;

public class ShoppingMgnt {
    private String product;
    private String price;
    private String company;
    private String note;
    private String purchase_date;

    public ShoppingMgnt(String price) {
        this.price = price;
    }

    public ShoppingMgnt(String product, String price) {
        this.product = product;
        this.price = price;
    }

    public ShoppingMgnt(String product, String price, String company, String note, String purchase_date) {
        this.product = product;
        this.price = price;
        this.company = company;
        this.note = note;
        this.purchase_date = purchase_date;
    }

    public String getProduct() {
        return product;
    }

    public String getPrice() {
        return price;
    }

    public String getCompany() {
        return company;
    }

    public String getNote() {
        return note;
    }

    public String getPurchase_date() {
        return purchase_date;
    }

    @Override
    public String toString() {
        return
                "Product: " + getProduct() +
                "\nPrice: " + getPrice() +
                "\nCompany: " + getCompany() +
                "\nNote: " + getNote() +
                "\nPurchase Date: " + getPurchase_date() + "\n";
    }
}
