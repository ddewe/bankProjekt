package com.company;

import java.util.ArrayList;

public class timerJob{

    private ArrayList<transactionClass> transactionList;
    private ArrayList<accountClass> accountList;

    public timerJob (ArrayList<transactionClass> transactionList, ArrayList<accountClass> accountList) {
        this.transactionList=transactionList;
        this.accountList=accountList;
    }

    public void runTimerJob() {


    }
}
