package com.company;

public class transactionClass{

    int sum;
    String fromCustomer, fromAccount, toAccount, transactionDate;

    public transactionClass (String fromCustomer, String fromAccount, String toAccount, int sum, String transactionDate) {
        this.fromCustomer = fromCustomer;
        this.fromAccount=fromAccount;
        this.toAccount=toAccount;
        this.sum=sum;
        this.transactionDate=transactionDate;
    }
}
