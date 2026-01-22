package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PrehranaTrackerForm {

    private JPanel mainPanel;
    private JTextField nazivField;
    private JTextField kalorijeField;
    private JButton addButton;
    private JButton backButton;
    private JList<String> foodList;
    private DefaultListModel<String> listModel;
    private JLabel totalLabel;

    private PrehranaService prehranaService;
    private String username;
    private List<FoodEntry> foodData;

    public PrehranaTrackerForm(PrehranaService prehranaService, String username) {
        this.prehranaService = prehranaService;
        this.username = username;
        this.foodData = prehranaService.getFoodData(username);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Prehrana Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(200, 70, 50));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 245, 250));

        JPanel nazivPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel nazivLabel = new JLabel("Naziv jela:");
        nazivLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nazivField = new JTextField(15);
        nazivField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        nazivPanel.add(nazivLabel);
        nazivPanel.add(nazivField);

        JPanel kalorijePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel kalorijeLabel = new JLabel("Kalorije:");
        kalorijeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        kalorijeField = new JTextField(10);
        kalorijeField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        kalorijePanel.add(kalorijeLabel);
        kalorijePanel.add(kalorijeField);

        addButton = new JButton("Dodaj");
        styleButton(addButton, new Color(76, 175, 80));

        listModel = new DefaultListModel<>();
        foodList = new JList<>(listModel);
        foodList.setFont(new Font("Consolas", Font.PLAIN, 14));
        foodList.setVisibleRowCount(8);
        foodList.setFixedCellHeight(25);
        foodList.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(foodList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Unesena hrana"));

        totalLabel = new JLabel("Ukupno kalorija danas: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(33, 150, 243));

        centerPanel.add(nazivPanel);
        centerPanel.add(kalorijePanel);
        centerPanel.add(addButton);
        centerPanel.add(scrollPane);
        centerPanel.add(totalLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(245, 245, 250));

        backButton = new JButton("Nazad");
        styleButton(backButton, new Color(244, 67, 54));
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        refreshDisplay();

        addButton.addActionListener(e -> {
            try {
                String naziv = nazivField.getText();
                int kalorije = Integer.parseInt(kalorijeField.getText());
                if (kalorije < 0 || naziv.isEmpty()) throw new NumberFormatException();

                prehranaService.addFood(username, naziv, kalorije);
                foodData = prehranaService.getFoodData(username);
                nazivField.setText("");
                kalorijeField.setText("");
                refreshDisplay();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Unesi validan broj kalorija!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e ->
                SwingUtilities.getWindowAncestor(mainPanel).dispose()
        );
    }

    private void refreshDisplay() {
        listModel.clear();
        int total = 0;
        for (FoodEntry f : foodData) {
            listModel.addElement(f.getNaziv() + " - " + f.getKalorije() + " kcal");
            total += f.getKalorije();
        }
        totalLabel.setText("Ukupno kalorija danas: " + total);
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
