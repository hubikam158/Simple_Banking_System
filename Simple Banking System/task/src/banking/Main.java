package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    private static boolean exit = false;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String url = "jdbc:sqlite:" + args[1];

        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER NOT NULL PRIMARY KEY," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0);");

                while (!exit) {
                    printMenuStart();
                    if (scanner.hasNextInt()) {
                        int choice = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice) {
                            case 1:
                                Account account = new Account();
                                statement.executeUpdate("INSERT INTO card (number,pin) VALUES ('" +
                                        account.getCardNumber() + "', '" + account.getPin() + "');");
                                System.out.println("\nYour card has been created" +
                                        "\nYour card number:\n" + account.getCardNumber() +
                                        "\nYour card PIN:\n" + account.getPin());
                                break;
                            case 2:
                                System.out.println("\nEnter your card number:");
                                String cardNumber = scanner.nextLine();
                                System.out.println("Enter your PIN:");
                                String pin = scanner.nextLine();
                                boolean logOut = false;

                                Account searchedAccount = null;
                                try (ResultSet findAccount = statement.executeQuery("SELECT number, pin, balance " +
                                        "FROM card")) {
                                    while (findAccount.next()) {
                                        if (cardNumber.equals(findAccount.getString("number")) &&
                                                pin.equals(findAccount.getString("pin"))) {
                                            searchedAccount = new Account(findAccount.getString("number"),
                                                    findAccount.getString("pin"), findAccount.getDouble("balance"));
                                            break;
                                        }
                                    }
                                }

                                if (searchedAccount != null) {
                                    System.out.println("\nYou have successfully logged in!");
                                    while (!logOut && !exit) {
                                        printMenuLogged();
                                        if (scanner.hasNextInt()) {
                                            int choiceLogged = scanner.nextInt();
                                            scanner.nextLine();
                                            switch (choiceLogged) {
                                                case 1:
                                                    System.out.println("\nBalance: " + searchedAccount.getBalance());
                                                    break;
                                                case 2:
                                                    System.out.println("\nEnter income:");
                                                    int income = scanner.nextInt();
                                                    scanner.nextLine();
                                                    searchedAccount.addIncome(income);
                                                    statement.executeUpdate("UPDATE card " +
                                                            "SET balance = balance + " + income +
                                                            " WHERE number = " + searchedAccount.getCardNumber());
                                                    System.out.println("Income was added!");
                                                    break;
                                                case 3:
                                                    System.out.println("\nTransfer\nEnter card number:");
                                                    String card = scanner.nextLine();
                                                    if (isValidByLuhn(card)) {
                                                        if (card.equals(searchedAccount.getCardNumber())) {
                                                            System.out.println("You can't transfer money to the same account!");
                                                        } else {
                                                            Account a = null;
                                                            try (ResultSet findAnotherAccount = statement.executeQuery("SELECT number, pin, balance " +
                                                                    "FROM card")) {
                                                                while (findAnotherAccount.next()) {
                                                                    if (card.equals(findAnotherAccount.getString("number"))) {
                                                                        a = new Account(findAnotherAccount.getString("number"),
                                                                                findAnotherAccount.getString("pin"),
                                                                                findAnotherAccount.getDouble("balance"));
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (a != null) {
                                                                System.out.println("Enter how much money you want to transfer:");
                                                                int value = scanner.nextInt();
                                                                scanner.nextLine();

                                                                if (searchedAccount.transfer(value)) {
                                                                    a.addIncome(value);
                                                                    statement.executeUpdate("UPDATE card " +
                                                                            "SET balance = balance - " + value +
                                                                            " WHERE number = " + searchedAccount.getCardNumber());
                                                                    statement.executeUpdate("UPDATE card " +
                                                                            "SET balance = balance + " + value +
                                                                            " WHERE number = " + a.getCardNumber());
                                                                    System.out.println("Success!");
                                                                } else {
                                                                    System.out.println("Not enough money!");
                                                                }
                                                            } else {
                                                                System.out.println("Such a card does not exist.");
                                                            }
                                                        }
                                                    } else {
                                                        System.out.println("Probably you made mistake in the card number. " +
                                                                "Please try again!");
                                                    }
                                                    break;
                                                case 4:
                                                    statement.executeUpdate("DELETE FROM card " +
                                                            "WHERE number = " + searchedAccount.getCardNumber());
                                                    System.out.println("\nThe account has been closed!");
                                                    logOut = true;
                                                    break;
                                                case 5:
                                                    logOut = true;
                                                    System.out.println("\nYou have successfully logged out!");
                                                    break;
                                                case 0:
                                                    exit = true;
                                                    break;
                                                default:
                                                    System.out.println("\nError, wrong choice!");
                                                    break;
                                            }
                                        } else {
                                            System.out.println("Wrong choice, try again!");
                                        }
                                    }
                                } else {
                                    System.out.println("\nWrong card number or PIN!");
                                }
                                break;
                            case 0:
                                exit = true;
                                break;
                            default:
                                System.out.println("\nError, wrong choice!");
                                break;
                        }
                        if (exit) {
                            System.out.println("\nBye!");
                        }
                    } else {
                        System.out.println("Wrong choice, try again!");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMenuStart() {
        System.out.println("\n1. Create an account" +
                "\n2. Log into account" +
                "\n0. Exit");
    }

    private static void printMenuLogged() {
        System.out.println("\n1. Balance" +
                "\n2. Add income" +
                "\n3. Do transfer" +
                "\n4. Close account" +
                "\n5. Log out" +
                "\n0. Exit");
    }

    private static boolean isValidByLuhn(String cardNumber) {
        int sum = 0;
        for (int i = 0; i < cardNumber.length() - 1; i++) {
            int value = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                sum += value < 5 ? value * 2 : value * 2 - 9;
            } else {
                sum += value;
            }
        }
        sum += Character.getNumericValue(cardNumber.charAt(cardNumber.length() - 1));
        return (10 - (sum % 10)) % 10 == 0;
    }
}
