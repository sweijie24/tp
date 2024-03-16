package seedu.budgetbuddy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SavingList {
    private static final Logger LOGGER = Logger.getLogger(SavingList.class.getName());
    protected ArrayList <Saving> savings;
    protected ArrayList<String> categories;
    protected double initialAmount;

    public SavingList() {
        this.savings = new ArrayList<>();
        this.categories = new ArrayList<>(Arrays.asList("Salary", 
        "Investments", "Gifts", "Others"));
        this.initialAmount = 0;

        LoggingConfig.configure();
    }

    public ArrayList<Saving> getSavings() {
        return savings;
    }

    public void findTotalSavings() {
        try {
            assert savings != null : "Savings list should not be null";

            double totalSavings = 0;
            for (int i = 0; i < savings.size(); i++) {
                Saving saving = savings.get(i);
                assert saving != null : "Saving object at index " + i + " is null";
                totalSavings += saving.getAmount();
            }

            this.initialAmount = totalSavings;
        } catch (AssertionError e) {
            LOGGER.log(Level.SEVERE, "Error occurred while calculating total savings", e);
        }
    }


    public void listSavings(String filterCategory, ExpenseList expenseList) {
        try {
            LOGGER.info("Listing savings...");

            findTotalSavings();
            System.out.println("Savings:");
            for (int i = 0; i < savings.size(); i++) {
                Saving saving = savings.get(i);
                if (filterCategory == null || saving.getCategory().equalsIgnoreCase(filterCategory)) {
                    System.out.print(i + 1 + " | ");
                    System.out.print("Category: " + saving.getCategory() + " | ");
                    System.out.print("Amount: $" + saving.getAmount() + " | ");
                }
            }
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Initial Savings Amount: $" + initialAmount);
            System.out.println("Expenses Deducted: ");

            double totalExpenses = 0;
            for (Expense expense : expenseList.getExpenses()) {
                totalExpenses += expense.getAmount();
                System.out.println("$" + expense.getAmount() + " spent on " + expense.getDescription() +
                        " on " + expense.getDateAdded());
            }
            System.out.println("------------------------------------------------------------------------");

            double remainingAmount = calculateRemainingSavings(initialAmount, totalExpenses);
            System.out.println("Remaining Amount: $" + remainingAmount);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while listing savings", e);
        }
    }

    public double calculateRemainingSavings(double initialAmount, double totalExpenses) {
        try {
            assert initialAmount >= 0 : "Initial amount should not be negative";
            assert totalExpenses >= 0 : "Total expenses should not be negative";

            double remainingAmount = initialAmount - totalExpenses;
            if (remainingAmount < 0) {
                throw new Exception("Insufficient Funds");
            } else {
                return remainingAmount;
            }
        } catch (AssertionError e) {
            LOGGER.log(Level.SEVERE, "Assertion failed while calculating remaining savings", e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Insufficient funds", e);
            throw new RuntimeException(e);
        }
        return -1;
    }

    public void addSaving(String category, String amount) {
        int amountInt = Integer.parseInt(amount);
        if (amountInt < 0) {
            try {
                throw new Exception("Savings should not be negative");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Saving saving = new Saving(category, amountInt);
        savings.add(saving);

        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public void editSaving(String category, int index, double amount) {
        int categoryIndex = categories.indexOf(category);
        if (categoryIndex != -1 && index > 0 && index <= savings.size()) {
            Saving savingToEdit = savings.get(index - 1);
            savingToEdit.setCategory(category);
            savingToEdit.setAmount(amount);
            System.out.println("Saving edited successfully.");
        } else {
            System.out.println("Invalid category or index.");
        }
    }

    public void reduceSavings(int index, double amount){
        if (index >= 0 && index < savings.size()){
            Saving saving = savings.get(index);
            if(saving.getAmount() >= amount){
                saving.setAmount(saving.getAmount() - amount);
            } else {
                System.out.println("Insufficient savings amount.");
            }
        } else {
            System.out.println("Invalid saving index.");
        }
    }
}
