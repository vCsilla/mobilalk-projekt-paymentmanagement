package com.example.paymentmanagement;

public class PaymentItem {
    private Money amount;       //amount of money
    private String id;          //paymentItem identifier
    private String item;        //the item itself

    public PaymentItem(Money amount, String id, String item){
        this.amount = amount;
        this.id = id;
        this.item = item;
    }

    public PaymentItem(){}

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
