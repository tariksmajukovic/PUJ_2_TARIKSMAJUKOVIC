package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SleepTrackerForm {

    private JPanel mainPanel;
    private JTextField hoursField;
    private JButton addButton;
    private JButton calculateAverageButton;
    private JButton backButton;
    private JList<String> sleepList;
    private DefaultListModel<String> listModel;
    private JLabel averageLabel;

    private SleepService sleepService;
    private String username;
    private List<Number> sleepData;

    public SleepTrackerForm(SleepService sleepService, String username) {
        this.sleepService = sleepService;
        this.username = username;
        this.sleepData = sleepService.getSleepData(username);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Sleep Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(45, 75, 140));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel label = new JLabel("Unesi sate spavanja:");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        centerPanel.add(label, gbc);

        gbc.gridx = 1;
        hoursField = new JTextField();
        hoursField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        hoursField.setPreferredSize(new Dimension(200, 40));
        centerPanel.add(hoursField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        addButton = new JButton("Dodaj");
        styleButton(addButton, new Color(76, 175, 80));
        centerPanel.add(addButton, gbc);

        gbc.gridy = 2;
        listModel = new DefaultListModel<>();
        sleepList = new JList<>(listModel);
        sleepList.setFont(new Font("Consolas", Font.PLAIN, 14));
        sleepList.setVisibleRowCount(8);
        sleepList.setFixedCellHeight(25);
        sleepList.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(sleepList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Uneseni sati spavanja"));
        centerPanel.add(scrollPane, gbc);

        gbc.gridy = 3;
        averageLabel = new JLabel("Prosjek spavanja: 0.00 sati");
        averageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        averageLabel.setForeground(new Color(33, 150, 243));
        centerPanel.add(averageLabel, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(new Color(245, 245, 250));

        calculateAverageButton = new JButton("Izračunaj prosjek");
        styleButton(calculateAverageButton, new Color(33, 150, 243));
        bottomPanel.add(calculateAverageButton);

        backButton = new JButton("Nazad");
        styleButton(backButton, new Color(244, 67, 54));
        bottomPanel.add(backButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        refreshDisplay();

        addButton.addActionListener(e -> {
            try {
                double hours = Double.parseDouble(hoursField.getText());
                if (hours < 0) throw new NumberFormatException();

                sleepService.addSleep(username, hours);
                sleepData = sleepService.getSleepData(username);
                hoursField.setText("");
                refreshDisplay();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Unesi validan broj sati!", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        calculateAverageButton.addActionListener(e -> {
            Double avg = sleepService.getAverage(username);
            if (avg != null) {
                averageLabel.setText(String.format("Prosjek spavanja: %.2f sati", avg));
            } else {
                averageLabel.setText("Prosjek spavanja: N/A");
            }
        });
        backButton.addActionListener(e ->
                SwingUtilities.getWindowAncestor(mainPanel).dispose()
        );
    }

    private void refreshDisplay() {
        listModel.clear();
        for (Number h : sleepData) {
            listModel.addElement(h.doubleValue() + " sati"); // sigurno pretvaranje
        }
        Double avg = sleepService.getAverage(username);
        if (avg != null) {
            averageLabel.setText(String.format("Prosjek spavanja: %.2f sati", avg));
        }
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
