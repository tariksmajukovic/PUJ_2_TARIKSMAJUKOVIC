package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.awt.*;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class EditForm {
    private JPanel mainPanel;
    private JTable dataTable;
    private JButton updateButton;
    private JButton deleteButton;

    private String username;
    private MongoDatabase db;
    private DefaultTableModel model;

    public EditForm(String username) {
        this.username = username;
        this.db = MongoDBConnection.getDatabase();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Edit User Data");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(200, 0, 0));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Polje", "Vrijednost"}, 0);
        dataTable = new JTable(model);
        dataTable.setRowHeight(28);
        dataTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = dataTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 0, 0));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 0), 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);

        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        styleButton(updateButton, new Color(200, 0, 0));
        styleButton(deleteButton, new Color(150, 0, 0));

        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        loadData();

        updateButton.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Odaberite red za ažuriranje!");
                return;
            }

            String field = (String) model.getValueAt(row, 0);
            String newValue = (String) model.getValueAt(row, 1);

            try {
                MongoCollection<Document> users = db.getCollection("users");
                users.updateOne(eq("username", username),
                        new Document("$set", new Document(field.replace("_", "."), parseValue(newValue))));
                JOptionPane.showMessageDialog(mainPanel, "Ažurirano polje: " + field);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "Greška pri ažuriranju: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            int row = dataTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(mainPanel, "Odaberite red za brisanje!");
                return;
            }

            String field = (String) model.getValueAt(row, 0);

            MongoCollection<Document> users = db.getCollection("users");
            users.updateOne(eq("username", username),
                    new Document("$unset", new Document(field.replace("_", "."), "")));

            model.removeRow(row);
            JOptionPane.showMessageDialog(mainPanel, "Obrisano polje: " + field);
        });
    }

    private void loadData() {
        model.setRowCount(0);

        MongoCollection<Document> users = db.getCollection("users");
        Document userDoc = users.find(eq("username", username)).first();

        if (userDoc != null) {
            if (userDoc.containsKey("username"))
                model.addRow(new Object[]{"username", userDoc.getString("username")});

            if (userDoc.containsKey("password"))
                model.addRow(new Object[]{"password", userDoc.getString("password")});

            if (userDoc.containsKey("sleepAverage"))
                model.addRow(new Object[]{"sleepAverage", safeToString(userDoc.get("sleepAverage"))});

            if (userDoc.containsKey("dailyCalories"))
                model.addRow(new Object[]{"dailyCalories", safeToString(userDoc.get("dailyCalories"))});

            if (userDoc.containsKey("fitnessCalories"))
                model.addRow(new Object[]{"fitnessCalories", safeToString(userDoc.get("fitnessCalories"))});

            if (userDoc.containsKey("foodEntries")) {
                List<Document> foods = (List<Document>) userDoc.get("foodEntries");
                for (int i = 0; i < foods.size(); i++) {
                    Document food = foods.get(i);
                    model.addRow(new Object[]{"foodEntries_" + i + "_naziv", safeToString(food.get("naziv"))});
                    model.addRow(new Object[]{"foodEntries_" + i + "_kalorije", safeToString(food.get("kalorije"))});
                }
            }

            if (userDoc.containsKey("fitnessEntries")) {
                List<Document> exercises = (List<Document>) userDoc.get("fitnessEntries");
                for (int i = 0; i < exercises.size(); i++) {
                    Document ex = exercises.get(i);
                    model.addRow(new Object[]{"fitnessEntries_" + i + "_aktivnost", safeToString(ex.get("aktivnost"))});
                    model.addRow(new Object[]{"fitnessEntries_" + i + "_kalorije", safeToString(ex.get("kalorije"))});
                }
            }

            if (userDoc.containsKey("sleepHours")) {
                List<Object> sleepHours = (List<Object>) userDoc.get("sleepHours");
                for (int i = 0; i < sleepHours.size(); i++) {
                    model.addRow(new Object[]{"sleepHours_" + i, safeToString(sleepHours.get(i))});
                }
            }
        }
    }

    private String safeToString(Object value) {
        return value == null ? "" : value.toString();
    }

    private Object parseValue(String value) {
        try {
            if (value.contains(".")) {
                return Double.parseDouble(value);
            } else {
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 35));
    }
}
