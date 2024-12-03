import java.io.*;
import java.util.*;

class Transaction {
    private String type; // Income or Expense
    private String date; // Format: YYYY-MM-DD
    private double amount;
    private String category;
    private String description;

    public Transaction(String type, String date, double amount, String category, String description) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-15s %-10.2f %-15s %-20s", type, date, amount, category, description);
    }
}

public class ExpenseTrackerApp {
    private List<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.txt";

    public static void main(String[] args) {
        ExpenseTrackerApp app = new ExpenseTrackerApp();
        app.loadTransactions();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Expense Tracker Menu ---");
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. Edit Transaction");
            System.out.println("4. Delete Transaction");
            System.out.println("5. Generate Report");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addTransaction(scanner);
                case 2 -> viewTransactions();
                case 3 -> editTransaction(scanner);
                case 4 -> deleteTransaction(scanner);
                case 5 -> generateReport();
                case 6 -> {
                    saveTransactions();
                    System.out.println("Exiting application. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private void addTransaction(Scanner scanner) {
        System.out.print("Enter type (Income/Expense): ");
        String type = scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        transactions.add(new Transaction(type, date, amount, category, description));
        System.out.println("Transaction added successfully!");
    }

    private void viewTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }

        System.out.println("\n--- Transactions ---");
        System.out.printf("%-10s %-15s %-10s %-15s %-20s\n", "Type", "Date", "Amount", "Category", "Description");
        for (int i = 0; i < transactions.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, transactions.get(i).toString());
        }
    }

    private void editTransaction(Scanner scanner) {
        viewTransactions();
        if (transactions.isEmpty()) return;

        System.out.print("Enter the transaction number to edit: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (index >= 0 && index < transactions.size()) {
            System.out.print("Enter new type (Income/Expense): ");
            String type = scanner.nextLine();
            System.out.print("Enter new date (YYYY-MM-DD): ");
            String date = scanner.nextLine();
            System.out.print("Enter new amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new category: ");
            String category = scanner.nextLine();
            System.out.print("Enter new description: ");
            String description = scanner.nextLine();

            transactions.set(index, new Transaction(type, date, amount, category, description));
            System.out.println("Transaction updated successfully!");
        } else {
            System.out.println("Invalid transaction number.");
        }
    }

    private void deleteTransaction(Scanner scanner) {
        viewTransactions();
        if (transactions.isEmpty()) return;

        System.out.print("Enter the transaction number to delete: ");
        int index = scanner.nextInt() - 1;

        if (index >= 0 && index < transactions.size()) {
            transactions.remove(index);
            System.out.println("Transaction deleted successfully!");
        } else {
            System.out.println("Invalid transaction number.");
        }
    }

    private void generateReport() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions to generate a report.");
            return;
        }

        double totalIncome = 0;
        double totalExpenses = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase("Income")) {
                totalIncome += transaction.getAmount();
            } else if (transaction.getType().equalsIgnoreCase("Expense")) {
                totalExpenses += transaction.getAmount();
            }
        }

        System.out.println("\n--- Financial Report ---");
        System.out.printf("Total Income: %.2f\n", totalIncome);
        System.out.printf("Total Expenses: %.2f\n", totalExpenses);
        System.out.printf("Net Balance: %.2f\n", totalIncome - totalExpenses);
    }

    private void saveTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 5) {
                    transactions.add(new Transaction(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("No previous transactions found.");
        }
    }
}
