package com.company;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class customerClass {

    public String firstName, lastName, ownerID;

    public customerClass (String firstName, String lastName, String ownerID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ownerID = ownerID;
    }
    public static String getOwnerID(Scanner tangentbord) {
        String input = "tooShort";
        while (true) {
            try {
                input = tangentbord.nextLine();
                if (input.length() == 10) {
                    return input;
                }
            } finally {
                if (input.length() != 10)
                    System.out.println("Felaktig längd. Ange 10 siffror.");
            }
        }
    }

    public ArrayList<accountClass> getAccounts(ArrayList<accountClass> accountList) {
        ArrayList<accountClass> customerAccounts = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            accountClass account = accountList.get(i);
            if (account.ownerID.equals(this.ownerID)) {
               customerAccounts.add(account);
            }
        }
        return customerAccounts;
    }
}
