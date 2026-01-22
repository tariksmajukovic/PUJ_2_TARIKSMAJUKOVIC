package org.example;

import javax.swing.*;
import java.awt.*;
import com.mongodb.client.MongoDatabase;
import org.example.finance.FinanceTrackerForm;

public class MyTrackersForm {
    private JPanel mainPanel;
    private JButton tracker1Button;
    private JButton tracker2Button;
    private JButton tracker3Button;
    private JButton tracker4Button;
    private JButton backButton;

    private String currentUsername;
    private SleepService sleepService;
    private PrehranaService prehranaService;
    private FitnessService fitnessService;

    public MyTrackersForm(String username) {
        this.currentUsername = username;

        MongoDatabase database = MongoDBConnection.getDatabase();
        sleepService = new SleepService(database);
        prehranaService = new PrehranaService(database);
        fitnessService = new FitnessService(database);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        mainPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("My Trackers");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(200, 0, 0)); // crvena
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        tracker1Button = new JButton("Sleep Tracker");
        tracker2Button = new JButton("Prehrana Tracker");
        tracker3Button = new JButton("Fitness Tracker");
        tracker4Button = new JButton("Finance Tracker");
        backButton     = new JButton("Back");

        styleButton(tracker1Button);
        styleButton(tracker2Button);
        styleButton(tracker3Button);
        styleButton(tracker4Button);
        styleButton(backButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(tracker1Button);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(tracker2Button);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(tracker3Button);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(tracker4Button);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(backButton);

        tracker1Button.addActionListener(e -> {
            SleepTrackerForm sleepForm = new SleepTrackerForm(sleepService, currentUsername);
            JFrame frame = new JFrame("Sleep Tracker");
            frame.setSize(500, 400);
            frame.setContentPane(sleepForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        tracker2Button.addActionListener(e -> {
            PrehranaTrackerForm prehranaForm = new PrehranaTrackerForm(prehranaService, currentUsername);
            JFrame frame = new JFrame("Prehrana Tracker");
            frame.setSize(500, 400);
            frame.setContentPane(prehranaForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            Integer dailyTotal = prehranaService.getDailyCalories(currentUsername);
            JOptionPane.showMessageDialog(frame,
                    "Ukupno kalorija danas: " + dailyTotal,
                    "Dnevni unos kalorija",
                    JOptionPane.INFORMATION_MESSAGE);

            frame.setVisible(true);
        });

        tracker3Button.addActionListener(e -> {
            FitnessTrackerForm fitnessForm = new FitnessTrackerForm(fitnessService, currentUsername);
            JFrame frame = new JFrame("Fitness Tracker");
            frame.setSize(500, 400);
            frame.setContentPane(fitnessForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            Integer dailyTotal = fitnessService.getDailyCalories(currentUsername);
            JOptionPane.showMessageDialog(frame,
                    "Ukupno potrošene kalorije danas: " + dailyTotal,
                    "Fitness kalorije",
                    JOptionPane.INFORMATION_MESSAGE);

            frame.setVisible(true);
        });

        tracker4Button.addActionListener(e -> {
            FinanceTrackerForm financeForm = new FinanceTrackerForm(currentUsername);
            JFrame frame = new JFrame("Finance Tracker");
            frame.setSize(700, 500);
            frame.setContentPane(financeForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        backButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(mainPanel).dispose();
        });
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 0, 0)); // crvena
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(220, 40));
        button.setMaximumSize(new Dimension(220, 40));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
