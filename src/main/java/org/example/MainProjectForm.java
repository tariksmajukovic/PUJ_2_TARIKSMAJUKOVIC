package org.example;

import javax.swing.*;
import java.awt.*;

public class MainProjectForm {
    private JPanel mainPanel;
    private JButton viewProfileButton;
    private JButton myTrackersButton;
    private JButton editButton;
    private JButton logOutButton;

    private String currentUsername;

    public MainProjectForm(String username) {
        this.currentUsername = username;

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        mainPanel.setBackground(new Color(245, 245, 250));

        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        viewProfileButton = new JButton("View Profile");
        myTrackersButton  = new JButton("My Trackers");
        editButton        = new JButton("Edit");
        logOutButton      = new JButton("Log Out");

        styleButton(viewProfileButton);
        styleButton(myTrackersButton);
        styleButton(editButton);
        styleButton(logOutButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(viewProfileButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(myTrackersButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(editButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(logOutButton);


        viewProfileButton.addActionListener(e -> {
            ProfileForm profileForm = new ProfileForm(currentUsername);
            JFrame frame = new JFrame("User Profile");
            frame.setSize(500, 400);
            frame.setContentPane(profileForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        myTrackersButton.addActionListener(e -> {
            MyTrackersForm trackersForm = new MyTrackersForm(currentUsername);
            JFrame frame = new JFrame("My Trackers");
            frame.setSize(500, 400);
            frame.setContentPane(trackersForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        editButton.addActionListener(e -> {
            EditForm editForm = new EditForm(currentUsername);
            JFrame frame = new JFrame("Edit Data");
            frame.setSize(600, 400);
            frame.setContentPane(editForm.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        logOutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "Logout!");
            SwingUtilities.getWindowAncestor(mainPanel).dispose();
        });
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
