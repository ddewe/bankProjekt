package com.company;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class transactionClass{

    int sum;
    String fromCustomer, fromAccount, toAccount;
    LocalDate transactionDate;

    public transactionClass (String fromCustomer, String fromAccount, String toAccount, int sum, LocalDate transactionDate) {
        this.fromCustomer = fromCustomer;
        this.fromAccount=fromAccount;
        this.toAccount=toAccount;
        this.sum=sum;
        this.transactionDate=transactionDate;
    }
}
