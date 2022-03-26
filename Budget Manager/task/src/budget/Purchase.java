package budget;

public class Purchase {
    private final String name;
    private final double price;
    private final PurchaseType type;

    public Purchase(String name, double price, PurchaseType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public PurchaseType getType() {
        return type;
    }
}
