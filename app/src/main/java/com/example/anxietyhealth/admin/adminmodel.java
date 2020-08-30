package com.example.anxietyhealth.admin;

public class adminmodel {
    public String amount,name,transactionid;

    public adminmodel() {
    }

    public adminmodel(String amount, String name, String transactionid) {
        this.amount = amount;
        this.name = name;
        this.transactionid = transactionid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }
}
