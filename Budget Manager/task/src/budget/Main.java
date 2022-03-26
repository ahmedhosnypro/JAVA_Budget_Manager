package budget;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static double balance = 0;
    private static final TreeMap<String, Double> purchases = new TreeMap<>();

    public static void main(String[] args) {
        startAction();
    }

    private static void startAction() {
        System.out.println("""
                
                Choose your action:
                1) Add income
                2) Add purchase
                3) Show list of purchases
                4) Balance
                0) Exit""");

        int action = Integer.parseInt(scanner.nextLine());
        System.out.println();

        switch (action) {
            case 1 -> addIncome();
            case 2 -> addPurchase();
            case 3 -> showPurchases();
            case 4 -> showBalance();
            case 0 -> System.out.println("Bye!");
            default -> throw new IllegalArgumentException();
        }
    }

    private static void addIncome() {
        System.out.println("Enter income:");
        double income = Double.parseDouble(scanner.nextLine());
        if (income > 0) {
            balance += income;
            System.out.println("Income was added!");
        }

        startAction();
    }

    private static void addPurchase() {
        System.out.println("Enter purchase name:");
        String purchaseName = scanner.nextLine();

        System.out.println("Enter its price:");
        double purchaseCost = Double.parseDouble(scanner.nextLine());

        purchases.put(purchaseName, purchaseCost);

        if (purchaseCost > 0) {
            if (balance - purchaseCost > 0) {
                balance -= purchaseCost;
            } else {
                balance = 0;
            }
        }

        startAction();
    }

    private static void showPurchases() {
        if (purchases.isEmpty()) {
            System.out.println("The purchase list is empty");
        } else {
            for (var purchase : purchases.entrySet()) {
                System.out.printf("%s: $%2f%n", purchase.getKey(), purchase.getValue());
            }
            System.out.printf("Balance: $%2f%n", balance);
        }
        startAction();
    }

    private static void showBalance() {
        System.out.printf("Balance: $%2f%n", balance);
        startAction();
    }
}
