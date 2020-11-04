package com.company;
import java.time.LocalDate;
import java.util.ArrayList;

public class timerJob {

    public static void main(ArrayList<transactionClass> transactionList, ArrayList<accountClass> accountList) {

        timerJob timerjob = new timerJob(transactionList, accountList);
        timerjob.runTimerJob();


    }

    public ArrayList<transactionClass> transactionList;
    public ArrayList<accountClass> accountList;

    public timerJob(ArrayList<transactionClass> transactionList, ArrayList<accountClass> accountList) {
        this.transactionList = transactionList;
        this.accountList = accountList;
    }

    public void runTimerJob() {

        for (int i = 0; i < transactionList.size(); i++) {

            transactionClass currentTransaction = transactionList.get(i);

            if (currentTransaction.transactionDate.compareTo(LocalDate.now()) <= 0) {
                for (int j = 0; j < accountList.size(); j++) {
                    accountClass toAccount = accountList.get(j);
                    if (currentTransaction.fromAccount.equals(toAccount.accountNumber)) {
                        toAccount.sum -= currentTransaction.sum;
                        System.out.println("Överföring från: " + toAccount.accountNumber + " till " +
                                currentTransaction.toAccount + " utförd.");
                        transactionList.remove(currentTransaction);
                    }
                }
            }
        }
    }
}
