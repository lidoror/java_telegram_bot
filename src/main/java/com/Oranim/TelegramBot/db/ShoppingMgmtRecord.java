package com.Oranim.TelegramBot.db;


public record ShoppingMgmtRecord(String product, String price, String company,
                                 String note, String purchaseDate, int columID) {



    @Override
    public String toString(){
        return
                "product: " + product +
                        "\nPrice: " + price +
                        "\nCompany: " + company+
                        "\nNote: " + note+
                        "\nPurchase Date: " + purchaseDate
                        +"\n--------\n";
    }
}
