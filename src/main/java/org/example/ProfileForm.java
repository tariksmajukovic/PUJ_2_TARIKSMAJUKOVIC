package org.example;

import javax.swing.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

import java.awt.*;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ProfileForm {
    private JPanel mainPanel;

    public ProfileForm(String username) {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 60));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 0, 0), 3),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(200, 0, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        Font dataFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color textColor = new Color(30, 30, 60);

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> usersCollection = db.getCollection("users");
        Document userDoc = usersCollection.find(eq("username", username)).first();

        JLabel usernameLabel = new JLabel("👤 Username: " + (userDoc != null ? userDoc.getString("username") : "N/A"));
        JLabel passwordLabel = new JLabel("🔑 Password: " + (userDoc != null ? userDoc.getString("password") : "N/A"));

        PrehranaService prehranaService = new PrehranaService(db);
        FitnessService fitnessService = new FitnessService(db);
        SleepService sleepService = new SleepService(db);

        int dailyCaloriesIn = prehranaService.getDailyCalories(username);
        int dailyCaloriesOut = fitnessService.getDailyCalories(username);
        Double avgSleep = sleepService.getAverage(username);
        if (avgSleep == null) avgSleep = 0.0;

        JLabel caloriesInLabel = new JLabel("🍎 Unesene kalorije: " + dailyCaloriesIn);
        JLabel caloriesOutLabel = new JLabel("🏃 Potrošene kalorije: " + dailyCaloriesOut);
        JLabel avgSleepLabel = new JLabel("😴 Prosječan san: " + avgSleep + " h");

        JLabel[] labels = {usernameLabel, passwordLabel, caloriesInLabel, caloriesOutLabel, avgSleepLabel};

        for (JLabel lbl : labels) {
            lbl.setFont(dataFont);
            lbl.setForeground(textColor);
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardPanel.add(Box.createVerticalStrut(10));
            cardPanel.add(lbl);
        }

        if (userDoc != null && userDoc.containsKey("foodEntries")) {
            cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            JLabel foodTitle = new JLabel("🍽️ Jela:");
            foodTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            foodTitle.setForeground(new Color(200, 0, 0));
            foodTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardPanel.add(foodTitle);

            List<Document> foods = (List<Document>) userDoc.get("foodEntries");
            for (Document food : foods) {
                String naziv = food.getString("naziv");
                Object kalorije = food.get("kalorije");
                JLabel foodLabel = new JLabel("• " + naziv + " (" + (kalorije != null ? kalorije.toString() : "0") + " kcal)");
                foodLabel.setFont(dataFont);
                foodLabel.setForeground(textColor);
                foodLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                cardPanel.add(foodLabel);
            }
        }

        if (userDoc != null && userDoc.containsKey("fitnessEntries")) {
            cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            JLabel fitnessTitle = new JLabel("💪 Aktivnosti:");
            fitnessTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            fitnessTitle.setForeground(new Color(200, 0, 0));
            fitnessTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardPanel.add(fitnessTitle);

            List<Document> activities = (List<Document>) userDoc.get("fitnessEntries");
            for (Document act : activities) {
                String aktivnost = act.getString("aktivnost");
                Object kalorije = act.get("kalorije");
                JLabel actLabel = new JLabel("• " + aktivnost + " (" + (kalorije != null ? kalorije.toString() : "0") + " kcal)");
                actLabel.setFont(dataFont);
                actLabel.setForeground(textColor);
                actLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                cardPanel.add(actLabel);
            }
        }

        MongoCollection<Document> transactionsCollection = db.getCollection("transactions");
        List<Document> transactionDocs = transactionsCollection.find(eq("username", username)).into(new java.util.ArrayList<>());

        if (!transactionDocs.isEmpty()) {
            cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            JLabel transTitle = new JLabel("💰 Transakcije:");
            transTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            transTitle.setForeground(new Color(200, 0, 0));
            transTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardPanel.add(transTitle);

            for (Document tr : transactionDocs) {
                String type = tr.getString("type");   // "prihod" ili "rashod"
                Object amount = tr.get("amount");
                String description = tr.getString("description"); // opis transakcije

                String text;
                if ("rashod".equalsIgnoreCase(type)) {
                    text = "• " + type + ": " + (amount != null ? amount.toString() : "0")
                            + (description != null ? " (" + description + ")" : "");
                } else {
                    text = "• " + type + ": " + (amount != null ? amount.toString() : "0");
                }

                JLabel trLabel = new JLabel(text);
                trLabel.setFont(dataFont);
                trLabel.setForeground(textColor);
                trLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                cardPanel.add(trLabel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
