package com.oranim.telegrambot.models;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record ExpensesModel(String product, String price, String category,
                            String note, String purchaseDate, int columID) {


    @Override
    public String toString(){
//        return """
//                Product: %s
//                Price: %s
//                Category: %s
//                Description: %s
//                Purchase Date: %s
//                """.formatted(product,price,category,note,purchaseDate)
        return outputFormat();
    }

    private String getFormattedPurchaseDate() {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(purchaseDate,inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(outputFormatter);
    }

    private boolean getFormattedNote() {
        return note.equals("No note added");
    }

    private String outputFormat() {
        String includeNote =
                """
                מוצר: %s
                מחיר: %s
                קטגוריה: %s
                תאור: %s
                תאריך רכישה: %s
                
                """.formatted(product,price,category,note,getFormattedPurchaseDate());

        String excludeFormat =
                """
               מוצר: %s
               מחיר: %s
               קטגוריה: %s
               תאריך רכישה: %s
               
               """.formatted(product,price,category,getFormattedPurchaseDate());
        return getFormattedNote() ? excludeFormat : includeNote;
    }

}
