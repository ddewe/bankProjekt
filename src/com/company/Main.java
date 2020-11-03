package com.company;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;

public class Main {

    public static Scanner tangentbord = new Scanner(System.in);
    static boolean validChoice = true;

    public static void main(String[] args) {

        //ToDo Ta bort betalningsuppdrag
        //ToDo Gör om publics till privates
        //ToDo Generell formatering
        //ToDo Inmatningskontroller, kraschhantering

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
        accountClass chosenAccount = null;

        //endregion


        //Creates files, reads from files to lists.
        startOfProgram(customerFile, transactionFile, accountFile, customerList, transactionList, accountList);

        if (args.length > 0 && args[0].equals("/sync")) {
            System.exit(0);
        }

        //region Main loop
        while (true) {

            printMenu(); //Prints the menu.

            switch (tangentbord.nextLine()) {
                case "0":
                    //region Avsluta
                    listsToFiles(customerFile, transactionFile, accountFile, customerList, transactionList, accountList);
                    System.out.println("Du avslutar.");
                    System.exit(0);
                    //endregion
                case "1":
                    //region Skapa kund
                    createCustomer(customerList);
                    customerListToFile(customerFile, customerList);
                    break;
                //endregion
                case "2":
                    //region Skapa konto
                    if (customerList.size() == 0) {
                        System.out.println("Inga kunder! Vill du skapa en?\n" +
                                "[1] Ja.\n" +
                                "[2] Nej, gå till menyval.");
                        choice = Integer.parseInt(tangentbord.nextLine());

                        if (choice == 1) {
                            createCustomer(customerList);
                        } else if (choice == 2) {
                            break;
                        }
                    }

                    listCustomers(customerList);

                    System.out.println("För vilken kund vill du skapa ett konto? Ange index.");

                    chosenCustomer = chooseCustomer(customerList, chosenCustomer);

                    System.out.println("Vald kund: " + chosenCustomer.firstName + " " + chosenCustomer.lastName + "\n");

                    System.out.print("Ange namn på kontot: ");
                    String accountName = tangentbord.nextLine();

                    int kontonr = new Random().nextInt(100000);
                    String accountNumber = "9159-" + kontonr;

                    accountClass ac = new accountClass(accountName, accountNumber, chosenCustomer.ownerID, 0);

                    accountList.add(ac);

                    System.out.println(accountName + " skapat.\n");

                    accountListToFile(accountFile, accountList);
                    break;
                //endregion
                case "3":
                    //region Lista kunder
                    if (customerList.size() == 0) {
                        System.out.println("Inga kunder! Vill du skapa en?\n" +
                                "[1] Ja.\n" +
                                "[2] Nej, gå till menyval.");
                        choice = Integer.parseInt(tangentbord.nextLine());

                        if (choice == 1) {
                            createCustomer(customerList);
                        } else if (choice == 2) {
                            break;
                        }
                    }

                    listCustomers(customerList);

                    break;
                //endregion
                case "4":
                    //region Sätt in pengar
                    handleMoney(accountFile, customerList, accountList, choice, chosenCustomer, accountFormat, 1, chosenAccount);
                    break;
                //endregion
                case "5":
                    //region Ta ut pengar
                    handleMoney(accountFile, customerList, accountList, choice, chosenCustomer, accountFormat, -1, chosenAccount);
                    break;
                //endregion
                case "6":
                    //region Lägg upp betalningsuppdrag

                    System.out.println("Från vilken kund? Ange index.\n");

                    listCustomers(customerList);

                    chosenCustomer = chooseCustomer(customerList, chosenCustomer);

                    String customerName = chosenCustomer.firstName + " " + chosenCustomer.lastName;

                    System.out.println("Från vilket konto? Ange index.\n");

                    listAccounts(accountList, chosenCustomer, accountFormat);


                    choice = Integer.parseInt(tangentbord.nextLine());

                    accountClass fromAccount = accountList.get(choice);

                    System.out.print("Vilket belopp?: ");

                    int chosenSum = getInteger(tangentbord);

                    fromAccount.sum = fromAccount.sum - chosenSum;

                    System.out.print("Mottagare?: ");

                    String toAccount = tangentbord.nextLine();

                    System.out.print("När ska överföringen ske? (dd/mm/yyyy): ");

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                    String transactionDateString = tangentbord.nextLine();

                    LocalDate transactionDate = LocalDate.parse(transactionDateString, dateTimeFormatter);

                    transactionClass transaction = new transactionClass(customerName, fromAccount.accountNumber, toAccount, chosenSum, transactionDate);

                    transactionList.add(transaction);

                    transactionListToFile(transactionFile, transactionList);
                    accountListToFile(accountFile, accountList);

                    break;

                //endregion
                case "7":
                    //region Ta bort betalningsuppdrag
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

                    transactionList.remove(choice);
                    transactionListToFile(transactionFile, transactionList);
                    break;

                //endregion
                case "8":
                    //region Visa kassavalv
                    int bankSum = 0;

                    for (int i = 0; i < accountList.size(); i++) {
                        bankSum = bankSum + accountList.get(i).sum;
                    }

                    System.out.println("Banken innehåller för tillfället: " + bankSum);

                    break;
                //endregion
                case "9":
                    //region Gör överföring

                    System.out.println("Från vilken kund? Ange index.\n");

                    listCustomers(customerList);

                    customerClass fromCustomer = chooseCustomer(customerList, chosenCustomer);

                    System.out.println("Från: " + fromCustomer.firstName + " " + fromCustomer.lastName + ". Från vilket konto? Ange index.\n");

                    listAccounts(accountList, fromCustomer, accountFormat);

                    fromAccount = chooseAccount(accountList, chosenAccount);

                    validateAccountOwner(accountList, fromCustomer, fromAccount);

                    System.out.println("\nFrån kund: " + fromCustomer.firstName + " " + fromCustomer.lastName + "\n" +
                            "Från konto: " + fromAccount.accountName + "\nTill vilken kund? Ange index.");

                    listCustomers(customerList);

                    customerClass toCustomer = chooseCustomer(customerList, chosenCustomer);

                    System.out.println("Till: " + toCustomer.firstName + " " + toCustomer.lastName + ". Till vilket konto? Ange index.");

                    listAccounts(accountList, toCustomer, accountFormat);

                    accountClass toAccountObject = chooseAccount(accountList, chosenAccount);

                    validateAccountOwner(accountList, toCustomer, toAccountObject);

                    System.out.println("Till kund: " + toCustomer.firstName + " " + toCustomer.lastName + "\n" +
                            "Till konto: " + toAccountObject.accountName + "\nHur mycket vill du föra över?");

                    chosenSum = Integer.parseInt(tangentbord.nextLine());

                    fromAccount.sum = fromAccount.sum - chosenSum;
                    toAccountObject.sum = toAccountObject.sum + chosenSum;

                    System.out.println("Överföring gjord.\nSumma på " + fromAccount.accountName + ": " + fromAccount.sum + "\n" +
                            "Summa på " + toAccountObject.accountName + ": " + toAccountObject.sum);

                    accountListToFile(accountFile, accountList);

                    break;

                //endregion
                default:
                    invalidInputDialog(tangentbord);
                    break;
            }
        }
        //endregion
    }


    //region Methods

    private static void startOfProgram(File customerFile, File transactionFile, File accountFile, ArrayList<customerClass> customerList, ArrayList<transactionClass> transactionList, ArrayList<accountClass> accountList) {
        createFiles(customerFile, transactionFile, accountFile);

        filesToLists(customerFile, transactionFile, accountFile, customerList, transactionList, accountList);

        timerJob.main(transactionList, accountList);
        transactionListToFile(transactionFile, transactionList);
    }

    private static void listsToFiles(File customerFile, File transactionFile, File accountFile,
                                     ArrayList<customerClass> customerList, ArrayList<transactionClass> transactionList, ArrayList<accountClass> accountList) {
        accountListToFile(accountFile, accountList);
        customerListToFile(customerFile, customerList);
        transactionListToFile(transactionFile, transactionList);
    }

    private static void invalidInputDialog(Scanner tangentbord) {
        System.out.println("Fel inmatning, skriva en siffra mellan 0-9.");
    }

    private static void handleMoney(File accountFile, ArrayList<customerClass> customerList,
                                    ArrayList<accountClass> accountList, int choice, customerClass chosenCustomer, String accountFormat,
                                    int sign, accountClass chosenAccount) {

        System.out.println("Vems konto?");

        listCustomers(customerList);

        chosenCustomer = chooseCustomer(customerList, chosenCustomer);

        System.out.println("\nVilket konto?\n");

        listAccounts(accountList, chosenCustomer, accountFormat);

        while (validChoice) {
            try {
                choice = Integer.parseInt(tangentbord.nextLine());
                chosenAccount = accountList.get(choice);

                chosenAccount = validateAccountOwner(accountList, chosenCustomer, chosenAccount);


                break;
            } catch (Exception e) {
                System.out.println("Felaktigt index, skriv endast siffran för index.");
            }
        }

        int chosenSum = 0;


        if (sign < 0) {
            do {
                System.out.println("Du har valt " + chosenAccount.accountName + ", " +
                        "Hur  mycket vill du ta ut?");
                chosenSum = Integer.parseInt(tangentbord.nextLine());
            }
            while (chosenAccount.sum < chosenSum);
        } else if (sign > 0) {
            System.out.println("Du har valt " + chosenAccount.accountName + ", " +
                    "Hur  mycket vill sätta in?");

            chosenSum = Integer.parseInt(tangentbord.nextLine());
        }

        chosenAccount.sum = chosenAccount.sum + (chosenSum * sign);

        System.out.println("Saldo på " + chosenAccount.accountName + " är nu " + chosenAccount.sum);

        accountListToFile(accountFile, accountList);

    }

    private static customerClass chooseCustomer(ArrayList<customerClass> customerList, customerClass chosenCustomer) {
        int choice;
        while (validChoice) {
            try {
                choice = Integer.parseInt(tangentbord.nextLine());
                chosenCustomer = customerList.get(choice);
                break;
            } catch (Exception e) {
                System.out.println("Felaktigt index, skriv endast siffran för index.");
            }
        }
        return chosenCustomer;
    }

    private static accountClass chooseAccount(ArrayList<accountClass> accountList, accountClass chosenAccount) {
        int choice;
        while (validChoice) {
            try {
                choice = Integer.parseInt(tangentbord.nextLine());
                chosenAccount = accountList.get(choice);
                break;
            } catch (Exception e) {
                System.out.println("Felaktigt index, skriv endast siffran för index.");
            }
        }
        return chosenAccount;
    }


    private static accountClass validateAccountOwner(ArrayList<accountClass> accountList, customerClass chosenCustomer, accountClass chosenAccount) {
        int choice;
        while (!chosenAccount.ownerID.equals(chosenCustomer.ownerID)) {
            try {
                System.out.println("Inte ägarens konto, ange ett listat index.");
                choice = Integer.parseInt(tangentbord.nextLine());
                chosenAccount = accountList.get(choice);
            } catch (Exception accountValidation) {
                System.out.println("Fel vid kontovalidering.");
            }
        }
        return chosenAccount;
    }

    private static void listAccounts(ArrayList<accountClass> accountList, customerClass chosenCustomer, String accountFormat) {
        System.out.format(accountFormat, "Index", "Kontonamn", "Saldo");
        for (int i = 0; i < accountList.size(); i++) {
            if (accountList.get(i).ownerID.equals(chosenCustomer.ownerID)) {
                System.out.format(accountFormat, i,
                        accountList.get(i).accountName,
                        accountList.get(i).sum);
            }
        }
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
                        LocalDate.parse(rowPart[4])
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

    //endregion
}
