package com.company;

import java.util.*;
import java.io.*;

public class Main {

    public static Scanner tangentbord = new Scanner(System.in);

    public static void main(String[] args) {

        //region Initializers
        File customerFile = new File("Customers.txt");
        File transactionFile = new File("Transactions.txt");
        File accountFile = new File("Accounts.txt");
        ArrayList<customerClass> customerList = new ArrayList<>();
        ArrayList<transactionClass> transactionList = new ArrayList<>();
        ArrayList<accountClass> accountList = new ArrayList<>();
        int choice = 0;
        customerClass chosenCustomer = null;
        String accountFormat = "%-8s %-15s %-10s\n";
        //endregion

        //Kollar om filer finns, annars skapar.
        createFiles(customerFile, transactionFile, accountFile);

        //Skriver från listor till filer
        filesToLists(customerFile, transactionFile, accountFile, customerList, transactionList, accountList);

        //Skriv ut meny med alternativ.
        printMenu();

        //Lista konton

        //region Inmatningskontroll
        String strVal = tangentbord.nextLine();
        boolean condition = false;
        while (condition == false) {

//            switch (tangentbord.nextLine()) {
//                case "0":
//                    quitProgram();
//                    break;
//                case "1":
//                    createCustomerDialog();
//                    break;
//                default:
//                    invalidInputDialog();
//                    break;
//            }
//            continueLoop();

            try {
                int val = Integer.parseInt(strVal);
                if (val == 0) {
                    //region Avsluta
                    System.out.println("Du avslutar.");
                    System.exit(0);
                    //endregion
                } else if (val == 1) {
                    //region Skapa kund
                    createCustomer(customerList);
                    customerListToFile(customerFile, customerList);
                    strVal = continueLoop(strVal);
                    //endregion
                } else if (val == 2) {
                    //region Skapa konto
                    if (customerList.size() == 0) {
                        System.out.println("Inga kunder! Vill du skapa en?\n" +
                                "[1] Ja.\n" +
                                "[2] Nej, gå till menyval.");
                        choice = Integer.parseInt(tangentbord.nextLine());

                        if (choice == 1) {
                            createCustomer(customerList);
                        } else if (choice == 2) {
                            strVal = continueLoop(strVal);
                        }
                    }

                    listCustomers(customerList);

                    System.out.println("För vilken kund vill du skapa ett konto? Ange index.");

                    choice = Integer.parseInt(tangentbord.nextLine());

                    System.out.println("Vald kund: " + customerList.get(choice).firstName + " " + customerList.get(choice).lastName + "\n");

                    chosenCustomer = customerList.get(choice);

                    System.out.print("Ange namn på kontot: ");
                    String accountName = tangentbord.nextLine();

                    int kontonr = new Random().nextInt(100000);
                    String accountNumber = "9159-" + kontonr;

                    accountClass ac = new accountClass(accountName, accountNumber, chosenCustomer.ownerID, 0);

                    accountList.add(ac);

                    System.out.println(accountName + " skapat.\n");

                    accountListToFile(accountFile, accountList);
                    strVal = continueLoop(strVal);
//endregion
                } else if (val == 3) {
                    //region Lista kunder
                    if (customerList.size() == 0) {
                        System.out.println("Inga kunder! Vill du skapa en?\n" +
                                "[1] Ja.\n" +
                                "[2] Nej, gå till menyval.");
                        choice = Integer.parseInt(tangentbord.nextLine());

                        if (choice == 1) {
                            createCustomer(customerList);
                        } else if (choice == 2) {
                            strVal = continueLoop(strVal);
                        }
                    }

                    listCustomers(customerList);

                    strVal = continueLoop(strVal);
                    //endregion
                } else if (val == 4) {
                    //region Sätt in pengar
                    handleMoney(accountFile, customerList, accountList, choice, chosenCustomer, accountFormat, 1);
                    strVal = continueLoop(strVal);
                    //endregion
                } else if (val == 5) {
                    //region Ta ut pengar

                    handleMoney(accountFile, customerList, accountList, choice, chosenCustomer, accountFormat, -1);

                    strVal = continueLoop(strVal);
                    //endregion
                } else if (val == 6) {
                    //region Lägg upp betalningsuppdrag

                    System.out.println("Från vilken kund? Ange index.\n");

                    listCustomers(customerList);

                    choice = Integer.parseInt(tangentbord.nextLine());

                    chosenCustomer = customerList.get(choice);

                    String customerName = chosenCustomer.firstName + " " + chosenCustomer.lastName;

                    System.out.println("Från vilket konto? Ange index.\n");

                    System.out.format(accountFormat, "Index", "Kontonamn", "Saldo");
                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).ownerID.equals(chosenCustomer.ownerID)) {
                            System.out.format(accountFormat, i,
                                    accountList.get(i).accountName,
                                    accountList.get(i).sum);
                        }
                    }

                    choice = Integer.parseInt(tangentbord.nextLine());

                    accountClass fromAccount = accountList.get(choice);

                    System.out.print("Vilket belopp?: ");

                    int chosenSum = getInteger(tangentbord);

                    fromAccount.sum = fromAccount.sum - chosenSum;

                    System.out.print("Mottagare?: ");

                    String toAccount = tangentbord.nextLine();

                    System.out.print("När ska överföringen ske?: ");

                    String transactionDate = tangentbord.nextLine();

                    transactionClass transaction = new transactionClass(customerName, fromAccount.accountName, toAccount, chosenSum, transactionDate);

                    transactionList.add(transaction);

                    transactionListToFile(transactionFile, transactionList);
                    accountListToFile(accountFile, accountList);

                    strVal = continueLoop(strVal);

                    //endregion
                } else if (val == 7) { //ta bort betalningsuppdrag

                    String transactionFormat = "%-8s %-30s %-15s %-15s %-8s %-16s\n";

                    System.out.println("Vilket betalningsuppdrag vill du ta bort? Ange index.\n");
                    System.out.printf(transactionFormat, "Index", "Kundens namn", "Kontonamn", "Mottagarnamn", "Belopp", "Överföringsdatum");

                    for (int i = 0; i < transactionList.size(); i++) {
                        System.out.printf(transactionFormat,
                                i,
                                transactionList.get(i).fromCustomer,
                                transactionList.get(i).fromAccount,
                                transactionList.get(i).toAccount,
                                transactionList.get(i).sum,
                                transactionList.get(i).transactionDate
                        );
                    }

                    choice = Integer.parseInt(tangentbord.nextLine());


                    //ToDo Ta bort betalningsuppdrag
                    for (int i = 0; i < customerList.size(); i++) {
                        /*
                        if (transactionList.get(i).fromCustomer && transactionList.get(i).fromAccount.equals(customerList.get(i).))

                         */
                    }

                    transactionList.remove(choice);


                } else if (val == 8) { //visa kassavalv
                    //region Visa kassavalv
                    int bankSum = 0;

                    for (int i = 0; i < accountList.size(); i++) {
                        bankSum = bankSum + accountList.get(i).sum;
                    }

                    System.out.println("Banken innehåller för tillfället: " + bankSum);

                    strVal = continueLoop(strVal);
                    //endregion
                } else if (val == 9) { // gör överföring mellan två konton
                    //region Gör överföring

                    System.out.println("Från vilken kund? Ange index.\n");

                    listCustomers(customerList);

                    choice = Integer.parseInt(tangentbord.nextLine());

                    customerClass fromCustomer = customerList.get(choice);

                    System.out.println("Från: " + fromCustomer.firstName + " " + fromCustomer.lastName + ". Från vilket konto? Ange index.\n");

                    System.out.format(accountFormat, "Index", "Kontonamn", "Saldo");
                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).ownerID.equals(fromCustomer.ownerID)) {
                            System.out.format(accountFormat, i,
                                    accountList.get(i).accountName,
                                    accountList.get(i).sum);
                        }
                    }

                    choice = Integer.parseInt(tangentbord.nextLine());

                    accountClass fromAccount = accountList.get(choice);

                    System.out.println("Från kund: " + fromCustomer.firstName + " " + fromCustomer.lastName + "\n" +
                            "Från konto: " + fromAccount.accountName + "\nTill vilken kund? Ange index.");

                    listCustomers(customerList);

                    choice = Integer.parseInt(tangentbord.nextLine());

                    customerClass toCustomer = customerList.get(choice);

                    System.out.println("Till: " + toCustomer.firstName + " " + toCustomer.lastName + ". Till vilket konto? Ange index.");

                    System.out.format(accountFormat, "Index", "Kontonamn", "Saldo");
                    for (int i = 0; i < accountList.size(); i++) {
                        if (accountList.get(i).ownerID.equals(toCustomer.ownerID)) {
                            System.out.format(accountFormat, i,
                                    accountList.get(i).accountName,
                                    accountList.get(i).sum);
                        }
                    }

                    choice = Integer.parseInt(tangentbord.nextLine());

                    accountClass toAccount = accountList.get(choice);

                    System.out.println("Till kund: " + toCustomer.firstName + " " + toCustomer.lastName + "\n" +
                            "Till konto: " + toAccount.accountName + "\nHur mycket vill du föra över?");

                    int chosenSum = Integer.parseInt(tangentbord.nextLine());

                    fromAccount.sum = fromAccount.sum - chosenSum;
                    toAccount.sum = toAccount.sum + chosenSum;

                    System.out.println("Överföring gjord.\nSumma på " + fromAccount.accountName + ": " + fromAccount.sum + "\n" +
                            "Summa på " + toAccount.accountName + ": " + toAccount.sum);


                    accountListToFile(accountFile, accountList);

                    strVal = continueLoop(strVal);

                    //endregion
                } else if (val < 0) {
                    System.out.println("För lågt! Välj ett menyalternativ mellan 0-9.");
                    strVal = tangentbord.nextLine();
                } else if (val > 9) {
                    System.out.println("För högt! Välj ett menyalternativ mellan 0-9.");
                    strVal = tangentbord.nextLine();
                }

            } catch (Exception wrongInput) {
                System.out.println("Ingen text, endast en siffra mellan 0-8.");
                strVal = tangentbord.nextLine();
            }
        }
        //endregion
        System.out.println("Ute ur loopen");
    }

    private static String continueLoop(String strVal) {
        System.out.println("\nVill du fortsätta eller avsluta?\n" +
                "[1]Fortsätt\n" +
                "[2]Avsluta");

        int cont = Integer.parseInt(tangentbord.nextLine());

        if (cont == 1) {
            printMenu();
            strVal = tangentbord.nextLine();
        } else if (cont == 2) {
            System.exit(0);
        }
        return strVal;
    }

    private static void handleMoney(File accountFile, ArrayList<customerClass> customerList,
                                    ArrayList<accountClass> accountList, int choice, customerClass chosenCustomer, String accountFormat,
                                    int sign) {

        System.out.println("Vems konto?");

        listCustomers(customerList);

        choice = Integer.parseInt(tangentbord.nextLine());

        chosenCustomer = customerList.get(choice);

        System.out.println("Vilket konto?\n");

        System.out.format(accountFormat, "Index", "Kontonamn", "Saldo");
        for (int i = 0; i < accountList.size(); i++) {
            if (accountList.get(i).ownerID.equals(chosenCustomer.ownerID)) {
                System.out.format(accountFormat, i,
                        accountList.get(i).accountName,
                        accountList.get(i).sum);
            }
        }

        choice = Integer.parseInt(tangentbord.nextLine());

        int chosenSum = 0;

        if (sign < 0) {
            do {
                System.out.println("Du har valt " + accountList.get(choice).accountName + ", " +
                        "Hur  mycket vill du ta ut?");
                chosenSum = Integer.parseInt(tangentbord.nextLine());
            }
            while (accountList.get(choice).sum < chosenSum);
        } else if (sign > 0) {
            System.out.println("Du har valt " + accountList.get(choice).accountName + ", " +
                    "Hur  mycket vill sätta in?");

            chosenSum = Integer.parseInt(tangentbord.nextLine());
        }

        accountList.get(choice).sum = accountList.get(choice).sum + (chosenSum * sign);

        System.out.println("Saldo på " + accountList.get(choice).accountName + " är nu " + accountList.get(choice).sum);

        accountListToFile(accountFile, accountList);
    }

    private static void accountListToFile(File accountFile, ArrayList<accountClass> accountList) {

        try {
            FileWriter accountWriter = new FileWriter(accountFile);
            for (int i = 0; i < accountList.size(); i++) {
                accountWriter.write(
                        accountList.get(i).accountName + ";" +
                                accountList.get(i).accountNumber + ";" +
                                accountList.get(i).ownerID + ";" +
                                accountList.get(i).sum + "\n");
            }
            accountWriter.close();
        } catch (Exception e) {
            System.out.println("Problem vid accountListToFile");
            System.out.println(e.getMessage());
        }
    }

    private static void customerListToFile(File customerFile, ArrayList<customerClass> customerList) {
        try {
            FileWriter fw = new FileWriter(customerFile);

            for (int i = 0; i < customerList.size(); i++) {
                fw.write(
                        customerList.get(i).firstName + ";" +
                                customerList.get(i).lastName + ";" +
                                customerList.get(i).ownerID + "\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("Problem vid customerListToFile");
            System.out.println(e.getMessage());
        }
    }

    private static void listCustomers(ArrayList<customerClass> customerList) {
        String listFormat = "%-5s %-15s %-15s %-10s\n";
        System.out.format(listFormat, "Index", "First name ", "Last name", "OwnerID");

        for (int i = 0; i < customerList.size(); i++) {

            System.out.format(listFormat,
                    i,
                    customerList.get(i).firstName,
                    customerList.get(i).lastName,
                    customerList.get(i).ownerID);
        }
    }

    private static void createCustomer(ArrayList<customerClass> customerList) {
        System.out.print("Ange förnamn: ");
        String firstName = tangentbord.nextLine();

        System.out.print("Ange efternamn: ");
        String lastName = tangentbord.nextLine();

        System.out.print("Ange personnummer, 10 siffror: ");
        String ownerID = customerClass.getOwnerID(tangentbord);

        customerList.add(new customerClass(firstName, lastName, ownerID));

    }

    private static int getInteger(Scanner tangentbord) {
        while (true) {
            try {
                return Integer.parseInt(tangentbord.nextLine());
            } catch (Exception e) {
                System.out.println("Ej ett heltal. Försök igen.");
            }
        }
    }

    private static void filesToLists(File customerFile, File transactionFile, File accountFile, ArrayList<customerClass> customerList,
                                     ArrayList<transactionClass> transactionList, ArrayList<accountClass> accountList) {
        customerFileToList(customerFile, customerList);
        transactionFileToList(transactionFile, transactionList);
        accountFileToList(accountFile, accountList);
    }

    private static void accountFileToList(File accountFile, ArrayList<accountClass> accountList) {
        try {
            Scanner accountReader = new Scanner(accountFile);
            while (accountReader.hasNextLine()) {
                String readRow = accountReader.nextLine();
                String[] rowPart = readRow.split(";");

                accountClass account = new accountClass(
                        rowPart[0],
                        rowPart[1],
                        (rowPart[2]),
                        Integer.parseInt(rowPart[3]));
                accountList.add(account);
            }
        } catch (Exception e) {
            System.out.println("Fel vid accountFiletoList");
            System.out.println(e.getMessage());
        }
    }

    private static void transactionFileToList(File transactionFile, ArrayList<transactionClass> transactionList) {
        try {
            Scanner transactionReader = new Scanner(transactionFile);
            while (transactionReader.hasNextLine()) {
                String readRow = transactionReader.nextLine();
                String[] rowPart = readRow.split(";");

                transactionClass transaction = new transactionClass(
                        rowPart[0],
                        rowPart[1],
                        rowPart[2],
                        Integer.parseInt(rowPart[3]),
                        rowPart[4]
                );
                transactionList.add(transaction);
            }
        } catch (Exception e) {
            System.out.println("Fel vid transactionFiletoList");
            System.out.println(e.getMessage());
        }
    }

    private static void customerFileToList(File customerFile, ArrayList<customerClass> customerList) {
        try {
            Scanner customerReader = new Scanner(customerFile);
            while (customerReader.hasNextLine()) {
                String readRow = customerReader.nextLine();
                String[] rowPart = readRow.split(";");

                customerClass customer = new customerClass(rowPart[0], rowPart[1], rowPart[2]);
                customerList.add(customer);
            }
        } catch (Exception e) {
            System.out.println("Fel vid customerFiletoList");
            System.out.println(e.getMessage());
        }
    }

    private static void createFiles(File customerFile, File transactionFile, File accountFile) {
        try {
            if (customerFile.exists() == false) customerFile.createNewFile();
            if (transactionFile.exists() == false) transactionFile.createNewFile();
            if (accountFile.exists() == false) accountFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Fel vid filskapning!\n" + e.getStackTrace());
        }
    }

    private static void printMenu() {
        System.out.println("*** Välkommen till NewtonBank! Vad vill du göra?\n" +
                "[0] Avsluta\n" +
                "[1] Skapa kund\n" +
                "[2] Skapa konto\n" +
                "[3] Lista kunder\n" +
                "[4] Sätt in pengar\n" +
                "[5] Ta ut pengar\n" +
                "[6] Lägg upp betalningsuppdrag\n" +
                "[7] Ta bort betalningsuppdrag\n" +
                "[8] Visa kassavalv\n" +
                "[9] Gör överföring mellan två konton ");
    }

    private static void transactionListToFile(File transactionFile, ArrayList<transactionClass> transactionList) {
        try {
            FileWriter fw = new FileWriter(transactionFile);

            for (int i = 0; i < transactionList.size(); i++) {
                fw.write(
                        transactionList.get(i).fromCustomer + ";" +
                                transactionList.get(i).fromAccount + ";" +
                                transactionList.get(i).toAccount + ";" +
                                transactionList.get(i).sum + ";" +
                                transactionList.get(i).transactionDate + "\n"
                );
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("Problem vid customerListToFile");
            System.out.println(e.getMessage());
        }
    }


}
