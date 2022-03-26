package budget;

import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static double balance = 0;
    private static final TreeMap<String, Double> allPurchases = new TreeMap<>();
    private static final TreeMap<String, Double> foodPurchases = new TreeMap<>();
    private static final TreeMap<String, Double> clothesPurchases = new TreeMap<>();
    private static final TreeMap<String, Double> entertainmentPurchases = new TreeMap<>();
    private static final TreeMap<String, Double> otherPurchases = new TreeMap<>();

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
        System.out.println("""
                
                Choose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                5) Back""");

        int purchaseType = Integer.parseInt(scanner.nextLine());
        System.out.println();

        switch (purchaseType) {
            case 1 -> addPurchaseUtility("Food");
            case 2 -> addPurchaseUtility("Clothes");
            case 3 -> addPurchaseUtility("Entertainment");
            case 4 -> addPurchaseUtility("Other");
            case 5 -> startAction();
            default -> throw new IllegalArgumentException();
        }
    }

    private static void addPurchaseUtility(String purchaseType) {

        System.out.println("Enter purchase name:");
        String purchaseName = scanner.nextLine();

        System.out.println("Enter its price:");
        double purchaseCost = Double.parseDouble(scanner.nextLine());

        allPurchases.put(purchaseName, purchaseCost);
        switch (purchaseType) {
            case "Food" -> foodPurchases.put(purchaseName, purchaseCost);
            case "Clothes" -> clothesPurchases.put(purchaseName, purchaseCost);
            case "Entertainment" -> entertainmentPurchases.put(purchaseName, purchaseCost);
            case "Other" -> otherPurchases.put(purchaseName, purchaseCost);
            default -> throw new IllegalArgumentException();
        }

        System.out.println("Purchase was added!");

        if (purchaseCost > 0) {
            if (balance - purchaseCost > 0) {
                balance -= purchaseCost;
            } else {
                balance = 0;
            }
        }

        addPurchase();
    }

    private static void showPurchases() {
        System.out.println("""
                
                Choose the type of purchases
                1) Food
                2) Clothes
                3) Entertainment
                4) Other
                5) All
                6) Back""");
        int purchaseType = Integer.parseInt(scanner.nextLine());
        System.out.println();

        switch (purchaseType) {
            case 1 -> showPurchasesUtility(foodPurchases);
            case 2 -> showPurchasesUtility(clothesPurchases);
            case 3 -> showPurchasesUtility(entertainmentPurchases);
            case 4 -> showPurchasesUtility(otherPurchases);
            case 5 -> showPurchasesUtility(allPurchases);
            case 6 -> startAction();
            default -> throw new IllegalArgumentException();
        }
    }

    private static void showPurchasesUtility(TreeMap<String, Double> purchases) {
        if (purchases.isEmpty()) {
            System.out.println("The purchase list is empty");
            if (allPurchases.isEmpty()) {
                startAction();
            } else {
                showPurchases();
            }
        } else {
            for (var purchase : purchases.entrySet()) {
                System.out.printf("%s: $%2f%n", purchase.getKey(), purchase.getValue());
            }
            System.out.printf("Total sum: $%2f%n", purchases.values().stream()
                    .reduce(0.0, Double::sum));

            showPurchases();
        }
    }


    private static void showBalance() {
        System.out.printf("Balance: $%2f%n", balance);
        startAction();
    }
}
