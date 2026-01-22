package org.example.finance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class FinanceTrackerForm {

    private JPanel mainPanel;
    private JButton addButton;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeCombo;
    private JTable transactionTable;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;
    private JButton clearButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JComboBox<String> categoryCombo;
    private JButton exportButton;
    private JLabel expenseFoodCategory;
    private JLabel expenseBillsCategory;
    private JLabel expenseFunCategory;
    private JLabel expenseTravelCategory;
    private JLabel expenseOtherCategory;

    private TransactionManager manager;
    private String username;

    public FinanceTrackerForm(String username) {
        this.username = username;
        manager = new TransactionManager(username);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        amountField = new JTextField(10);
        descriptionField = new JTextField(15);
        typeCombo = new JComboBox<>();
        categoryCombo = new JComboBox<>();
        transactionTable = new JTable();
        incomeLabel = new JLabel("Prihodi: 0");
        expenseLabel = new JLabel("Rashodi: 0");
        balanceLabel = new JLabel("Saldo: 0");
        clearButton = new JButton("Clear");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        exportButton = new JButton("Export");
        addButton = new JButton("Add");

        expenseFoodCategory = new JLabel("Hrana: 0");
        expenseBillsCategory = new JLabel("Racuni: 0");
        expenseFunCategory = new JLabel("Zabava: 0");
        expenseTravelCategory = new JLabel("Prevoz: 0");
        expenseOtherCategory = new JLabel("Ostalo: 0");

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Tip", "Iznos", "Opis", "Kategorija"}, 0);
        transactionTable.setModel(model);


        typeCombo.addItem("Prihod");
        typeCombo.addItem("Rashod");

        categoryCombo.addItem("Plata");
        categoryCombo.addItem("Hrana");
        categoryCombo.addItem("Racuni");
        categoryCombo.addItem("Zabava");
        categoryCombo.addItem("Prevoz");
        categoryCombo.addItem("Ostalo");


        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Tip:"));
        inputPanel.add(typeCombo);
        inputPanel.add(new JLabel("Iznos:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Opis:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Kategorija:"));
        inputPanel.add(categoryCombo);
        inputPanel.add(addButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exportButton);

        JPanel summaryPanel = new JPanel();
        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);
        summaryPanel.add(balanceLabel);

        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        categoryPanel.add(expenseFoodCategory);
        categoryPanel.add(expenseBillsCategory);
        categoryPanel.add(expenseFunCategory);
        categoryPanel.add(expenseTravelCategory);
        categoryPanel.add(expenseOtherCategory);

        mainPanel.add(inputPanel);
        mainPanel.add(new JScrollPane(transactionTable));
        mainPanel.add(buttonPanel);
        mainPanel.add(summaryPanel);
        mainPanel.add(categoryPanel);

        loadDataIntoTable();
        updateSummary();
        updateCategoryExpenseLabels();

        addButton.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionField.getText();
                String category = (String) categoryCombo.getSelectedItem();

                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Opis ne smije biti prazan");
                    return;
                }

                Transaction t = new Transaction(type, amount, description, category);
                manager.addTransaction(t);

                loadDataIntoTable();
                updateSummary();
                updateCategoryExpenseLabels();

                amountField.setText("");
                descriptionField.setText("");
                categoryCombo.setSelectedIndex(0);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Vrijednost mora biti broj");
            }
        });

        clearButton.addActionListener(e -> {
            manager.clearAllTransactions();
            loadDataIntoTable();
            updateSummary();
            updateCategoryExpenseLabels();
        });

        updateButton.addActionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Odaberite transakciju iz tabele!");
                return;
            }

            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Iznos ne smije biti prazan!");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Iznos mora biti broj!");
                return;
            }

            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Opis ne smije biti prazan!");
                return;
            }

            String type = (String) typeCombo.getSelectedItem();
            String category = (String) categoryCombo.getSelectedItem();

            Transaction updated = new Transaction(type, amount, description, category);
            manager.updateTransaction(updated, selectedRow);

            loadDataIntoTable();
            updateSummary();
            updateCategoryExpenseLabels();

            amountField.setText("");
            descriptionField.setText("");
            categoryCombo.setSelectedIndex(0);

            JOptionPane.showMessageDialog(null, "Transakcija ažurirana!");
        });

        transactionTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow != -1) {
                Transaction t = manager.getAllTransactions().get(selectedRow);
                amountField.setText(String.valueOf(t.getAmount()));
                descriptionField.setText(t.getDescription());
                typeCombo.setSelectedItem(t.getType());
                categoryCombo.setSelectedItem(t.getCategory());
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Odaberite transakciju za brisanje!");
                return;
            }

            int choice = JOptionPane.showConfirmDialog(null,
                    "Jeste li sigurni da želite izbrisati ovu transakciju?",
                    "Potvrda brisanja",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                manager.deleteTransaction(selectedRow);
                loadDataIntoTable();
                updateSummary();
                updateCategoryExpenseLabels();
                JOptionPane.showMessageDialog(null, "Transakcija izbrisana!");
            }
        });

        exportButton.addActionListener(e -> {
            try {
                exportDataToTXT();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Došlo je do greške pri izvozu podataka.");
                ex.printStackTrace();
            }
        });
    }

    private void exportDataToTXT() throws IOException {
        File file = new File("financial_report.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Tip | Iznos | Opis | Kategorija\n");

            for (Transaction t : manager.getAllTransactions()) {
                writer.write(t.getType() + " | "
                        + t.getAmount() + " | "
                        + t.getDescription() + " | "
                        + t.getCategory() + "\n");
            }

            writer.write("\nUkupni prihodi: " + manager.getTotalIncome() + "\n");
            writer.write("Ukupni rashodi: " + manager.getTotalExpense() + "\n");
            writer.write("Saldo: " + (manager.getTotalIncome() - manager.getTotalExpense()) + "\n");

            Map<String, Double> categoryExpenses = manager.getTotalExpenseByCategory();
            writer.write("\nUkupni iznos po kategorijama:\n");
            for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }

            JOptionPane.showMessageDialog(null, "Izveštaj je uspešno eksportovan u 'financial_report.txt'.");
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void loadDataIntoTable() {
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);

        ArrayList<Transaction> transactions = manager.getAllTransactions();
        for (Transaction t : transactions) {
            model.addRow(new Object[]{t.getType(), t.getAmount(), t.getDescription(), t.getCategory()});
        }

        transactionTable.revalidate();
        transactionTable.repaint();
    }

    private void updateSummary() {
        incomeLabel.setText("Prihodi: " + manager.getTotalIncome());
        expenseLabel.setText("Rashodi: " + manager.getTotalExpense());
        balanceLabel.setText("Saldo: " + (manager.getTotalIncome() - manager.getTotalExpense()));
    }

    private void updateCategoryExpenseLabels() {
        Map<String, Double> categoryExpenses = manager.getTotalExpenseByCategory();

        expenseFoodCategory.setText("Hrana: " + categoryExpenses.get("Hrana"));
        expenseBillsCategory.setText("Racuni: " + categoryExpenses.get("Racuni"));
        expenseFunCategory.setText("Zabava: " + categoryExpenses.get("Zabava"));
        expenseTravelCategory.setText("Prevoz: " + categoryExpenses.get("Prevoz"));
        expenseOtherCategory.setText("Ostalo: " + categoryExpenses.get("Ostalo"));
    }
}
