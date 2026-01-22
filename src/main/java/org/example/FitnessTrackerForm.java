package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class FitnessTrackerForm {

    private JPanel mainPanel;
    private JTextField aktivnostField;
    private JTextField trajanjeField;
    private JComboBox<String> intenzitetBox;
    private JButton addButton;
    private JButton backButton;
    private JList<String> fitnessList;
    private DefaultListModel<String> listModel;
    private JLabel totalLabel;

    private FitnessService fitnessService;
    private String username;
    private List<FitnessEntry> fitnessData;

    public FitnessTrackerForm(FitnessService fitnessService, String username) {
        this.fitnessService = fitnessService;
        this.username = username;
        this.fitnessData = fitnessService.getFitnessData(username);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Fitness Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(50, 150, 50));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 245, 250));

        JPanel aktivnostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel aktivnostLabel = new JLabel("Aktivnost:");
        aktivnostLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        aktivnostField = new JTextField(15);
        aktivnostField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        aktivnostPanel.add(aktivnostLabel);
        aktivnostPanel.add(aktivnostField);

        JPanel trajanjePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel trajanjeLabel = new JLabel("Trajanje (min):");
        trajanjeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        trajanjeField = new JTextField(5);
        trajanjeField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        trajanjePanel.add(trajanjeLabel);
        trajanjePanel.add(trajanjeField);

        JPanel intenzitetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel intenzitetLabel = new JLabel("Intenzitet:");
        intenzitetLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        intenzitetBox = new JComboBox<>(new String[]{"low", "medium", "high"});
        intenzitetBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        intenzitetPanel.add(intenzitetLabel);
        intenzitetPanel.add(intenzitetBox);

        addButton = new JButton("Dodaj");
        styleButton(addButton, new Color(76, 175, 80));

        listModel = new DefaultListModel<>();
        fitnessList = new JList<>(listModel);
        fitnessList.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(fitnessList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Unesene aktivnosti"));

        totalLabel = new JLabel("Ukupno potrošene kalorije danas: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(new Color(33, 150, 243));

        centerPanel.add(aktivnostPanel);
        centerPanel.add(trajanjePanel);
        centerPanel.add(intenzitetPanel);
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
                String aktivnost = aktivnostField.getText();
                int trajanje = Integer.parseInt(trajanjeField.getText());
                String intenzitet = (String) intenzitetBox.getSelectedItem();

                if (trajanje <= 0 || aktivnost.isEmpty()) throw new NumberFormatException();

                fitnessService.addActivity(username, aktivnost, trajanje, intenzitet);
                fitnessData = fitnessService.getFitnessData(username);
                aktivnostField.setText("");
                trajanjeField.setText("");
                refreshDisplay();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Unesi validne podatke!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e ->
                SwingUtilities.getWindowAncestor(mainPanel).dispose()
        );
    }

    private void refreshDisplay() {
        listModel.clear();
        int total = 0;
        for (FitnessEntry f : fitnessData) {
            listModel.addElement(f.toString());
            total += f.getKalorije();
        }
        totalLabel.setText("Ukupno potrošene kalorije danas: " + total);
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
