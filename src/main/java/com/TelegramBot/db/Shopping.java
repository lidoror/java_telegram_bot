package com.TelegramBot.db;

import java.sql.Date;
import java.time.LocalDate;

public class Shopping {
    private String product;
    private int price;
    private String company;
    private String note;
    private LocalDate purchaseDate;

    public Shopping() {}

    public Shopping(String product, int price, String company, String note, LocalDate purchaseDate) {
        this.product = product;
        this.price = price;
        this.company = company;
        this.note = note;
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "Shopping{" +
                "product= " + product +
                "\nprice= " + price +
                "\ncompany= " + company +
                "\nnote= " + note +
                "\npurchaseDate = " + purchaseDate +
                '}';
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
