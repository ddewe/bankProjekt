package com.company;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.InputMismatchException;
import java.util.Scanner;

public class customerClass {

    String firstName, lastName, ownerID;

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
}
