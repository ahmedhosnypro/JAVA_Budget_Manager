package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static double balance = 0;

    private static final ArrayList<Purchase> purchases = new ArrayList<>();

    private static final String SAVE_PATH = "purchases.txt";
    private static final File saveFile = new File(SAVE_PATH);

    private static final String EMPTY_MESSAGE = "The purchase list is empty";

    private static final Consumer<Purchase> purchasePrinter = p ->
            System.out.printf("%s $%.2f%n", p.getName(), p.getPrice());

    private static final Comparator<Purchase> purchaseComparator = (p1, p2) ->
            Double.compare(p2.getPrice(), p1.getPrice());


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
                5) Save
                6) Load
                7) Analyze (Sort)
                0) Exit""");

        int action = Integer.parseInt(scanner.nextLine());
        System.out.println();

        switch (action) {
            case 1 -> addIncome();
            case 2 -> addPurchase();
            case 3 -> showPurchases();
            case 4 -> showBalance();
            case 5 -> save();
            case 6 -> load();
            case 7 -> sort();
            case 0 -> {
                System.out.println("Bye!");
                System.exit(0);
            }
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
            case 1 -> addPurchaseUtility(PurchaseType.FOOD);
            case 2 -> addPurchaseUtility(PurchaseType.CLOTHES);
            case 3 -> addPurchaseUtility(PurchaseType.ENTERTAINMENT);
            case 4 -> addPurchaseUtility(PurchaseType.OTHER);
            case 5 -> startAction();
            default -> throw new IllegalArgumentException();
        }
    }

    private static void addPurchaseUtility(PurchaseType purchaseType) {

        System.out.println("Enter purchase name:");
        String purchaseName = scanner.nextLine();

        System.out.println("Enter its price:");
        double purchasePrice = Double.parseDouble(scanner.nextLine());

        purchases.add(new Purchase(purchaseName, purchasePrice, purchaseType));

        System.out.println("Purchase was added!");

        if (purchasePrice > 0) {
            if (balance - purchasePrice > 0) {
                balance -= purchasePrice;
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
            case 1 -> showPurchasesUtility(PurchaseType.FOOD);
            case 2 -> showPurchasesUtility(PurchaseType.CLOTHES);
            case 3 -> showPurchasesUtility(PurchaseType.ENTERTAINMENT);
            case 4 -> showPurchasesUtility(PurchaseType.OTHER);
            case 5 -> showAllPurchases();
            case 6 -> startAction();
            default -> throw new IllegalArgumentException();
        }
    }

    private static void showPurchasesUtility(PurchaseType purchaseType) {
        if (purchases.isEmpty()) {
            System.out.println(EMPTY_MESSAGE);
            startAction();
        } else {
            if (purchases.stream().noneMatch(p -> p.getType().equals(purchaseType))) {
                System.out.println(EMPTY_MESSAGE);
            } else {
                purchases.stream()
                        .filter(p -> p.getType().equals(purchaseType))
                        .forEach(purchasePrinter);
                System.out.printf("Total sum: $%.2f%n", getTotal(purchaseType));
            }
            showPurchases();
        }
    }

    private static void showAllPurchases() {
        if (purchases.isEmpty()) {
            System.out.println(EMPTY_MESSAGE);
            startAction();
        } else {
            purchases.forEach(purchasePrinter);
            System.out.printf("Total sum: $%.2f%n", getTotal());
        }
        showPurchases();
    }

    private static void showBalance() {
        System.out.printf("Balance: $%.2f%n", balance);
        startAction();
    }

    private static void save() {
        try (FileWriter writer = new FileWriter(saveFile)) {
            writer.write(balance + "\n");
            int i = 0;
            for (var purchase : purchases) {
                if (i == purchases.size() - 1) {
                    writer.write(purchase.getName() + " $" + purchase.getPrice() + " #" + purchase.getType().toString());
                } else {
                    writer.write(purchase.getName() + " $" + purchase.getPrice() + " #" + purchase.getType().toString() + "\n");
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Purchases were saved!");
        startAction();
    }

    private static void load() {
        try (Scanner fileScanner = new Scanner(saveFile)) {
            if (fileScanner.hasNext()) {
                balance = Double.parseDouble(fileScanner.nextLine());
            }
            while (fileScanner.hasNext()) {
                String purchase = fileScanner.nextLine();

                purchases.add(new Purchase(purchase.substring(0, purchase.lastIndexOf("$") - 1), Double.parseDouble(purchase.substring(purchase.lastIndexOf("$") + 1, purchase.lastIndexOf(" "))), PurchaseType.valueOf(purchase.substring(purchase.lastIndexOf("#") + 1))));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Purchases were loaded!");
        startAction();
    }

    private static void sort() {
        System.out.println("""
                                
                How do you want to sort?
                1) Sort all purchases
                2) Sort by type
                3) Sort certain type
                4) Back""");

        int sorting = Integer.parseInt(scanner.nextLine());
        System.out.println();

        switch (sorting) {
            case 1 -> sortAll();
            case 2 -> sortByType();
            case 3 -> sortCertainType();
            case 4 -> startAction();
            default -> throw new IllegalArgumentException();
        }
    }

    private static void sortAll() {
        if (purchases.isEmpty()) {
            System.out.println(EMPTY_MESSAGE);
        } else {
            purchases.stream()
                    .sorted(purchaseComparator)
                    .forEach(purchasePrinter);
            System.out.printf("Total: $%.2f%n", getTotal());
        }
        sort();
    }

    private static void sortByType() {
        double foodTotal = getTotal(PurchaseType.FOOD);
        double clothesTotal = getTotal(PurchaseType.CLOTHES);
        double entertainmentTotal = getTotal(PurchaseType.ENTERTAINMENT);
        double otherTotal = getTotal(PurchaseType.OTHER);


        ArrayList<Purchase> totals = new ArrayList<>(
                List.of(new Purchase("Food -", foodTotal, PurchaseType.FOOD),
                        new Purchase("Clothes -", clothesTotal, PurchaseType.CLOTHES),
                        new Purchase("Entertainment -", entertainmentTotal, PurchaseType.ENTERTAINMENT),
                        new Purchase("Other -", otherTotal, PurchaseType.OTHER)));

        totals.sort(purchaseComparator);
        totals.forEach(purchasePrinter);
        System.out.printf("Total sum: $%.2f%n", getTotal());

        sort();
    }

    private static void sortCertainType() {
        System.out.println("""
                                
                Choose the type of purchase
                1) Food
                2) Clothes
                3) Entertainment
                4) Other""");

        int purchaseType = Integer.parseInt(scanner.nextLine());
        System.out.println();

        switch (purchaseType) {
            case 1 -> sortCertainTypeUtility(PurchaseType.FOOD);
            case 2 -> sortCertainTypeUtility(PurchaseType.CLOTHES);
            case 3 -> sortCertainTypeUtility(PurchaseType.ENTERTAINMENT);
            case 4 -> sortCertainTypeUtility(PurchaseType.OTHER);
            default -> throw new IllegalArgumentException();
        }

        sort();
    }

    private static void sortCertainTypeUtility(PurchaseType purchaseType) {
        if (purchases.stream().noneMatch(p -> p.getType().equals(purchaseType))) {
            System.out.println(EMPTY_MESSAGE);
        } else {
            purchases.stream()
                    .filter(p -> p.getType().equals(purchaseType))
                    .sorted(purchaseComparator)
                    .forEach(purchasePrinter);
            System.out.printf("Total sum: $%.2f%n", getTotal(purchaseType));
        }
        sort();
    }

    private static double getTotal(PurchaseType purchaseType) {
        return purchases.stream()
                .filter(p -> p.getType().equals(purchaseType))
                .mapToDouble(Purchase::getPrice)
                .reduce(0.0, Double::sum);
    }

    private static double getTotal() {
        return purchases.stream()
                .mapToDouble(Purchase::getPrice)
                .reduce(0.0, Double::sum);
    }
}