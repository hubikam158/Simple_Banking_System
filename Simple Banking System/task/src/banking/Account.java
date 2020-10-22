package banking;

import java.util.Random;

public class Account {

    private String cardNumber;
    private String pin;
    private double balance = 0;

    public Account() {
        this.cardNumber = createCardNumber();
        this.pin = createPin();
    }

    public Account(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    private String createCardNumber() {
        Random random = new Random();
        StringBuilder s = new StringBuilder("400000");
        int userId = random.nextInt(999999990) + 10;
        s.append(String.format("%09d", userId));
        int sum = 0;

        for (int i = 0; i < s.length(); i++) {
            int value = Character.getNumericValue(s.codePointAt(i));
            if (i % 2 == 0) {
                int newValue = value < 5 ? value * 2 : value * 2 - 9;
                sum += newValue;
            } else {
                sum += value;
            }
        }
        int checkSum = (10 - (sum % 10)) % 10;
        return s.toString() + checkSum;
    }

    private String createPin() {
        Random random = new Random();
        int pin = random.nextInt(10000);
        return String.format("%04d", pin);
    }

    public void addIncome(int value) {
        this.balance += value;
    }

    public boolean transfer(int value) {
        if (this.balance >= value) {
            this.balance -= value;
            return true;
        }
        return false;
    }
}
