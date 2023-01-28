package com.oranim.telegrambot.db;


public record ShoppingMgmtRecord(String product, String price, String category,
                                 String note, String purchaseDate, int columID) {



    @Override
    public String toString(){
        return
                "product: " + product +
                        "\nPrice: " + price +
                        "\nCategory: " + category +
                        "\nNote: " + note+
                        "\nPurchase Date: " + purchaseDate;
    }
}
