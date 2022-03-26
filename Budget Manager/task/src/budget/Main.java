package budget;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double total = 0;
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            System.out.println(input);
            total += Double.parseDouble(input.substring(input.indexOf("$") + 1));
        }
        System.out.printf("Total: $%2f", total);
    }
}
