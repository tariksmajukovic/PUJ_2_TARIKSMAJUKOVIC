package org.example;

import javax.swing.*;
import java.awt.*;
import org.example.UserService;

public class MainForm {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;
    private JPanel mainPanel;
    private UserService service;
    private JFrame parentFrame;

    public MainForm() {
        service = new UserService();

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        mainPanel.setBackground(new Color(245, 245, 250));

        JLabel titleLabel = new JLabel("Welcome");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField = new JTextField(15);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        registerButton = new JButton("Register");
        loginButton = new JButton("Login");

        styleButton(registerButton);
        styleButton(loginButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(userLabel);
        mainPanel.add(usernameField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(passLabel);
        mainPanel.add(passwordField);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainPanel, "Unesite username i password!");
            } else {
                boolean ok = service.register(username, password);
                JOptionPane.showMessageDialog(mainPanel, ok ? "Registracija uspješna" : "Korisnik već postoji");
                if (ok) {
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean ok = service.login(username, password);
            if (ok) {
                JOptionPane.showMessageDialog(mainPanel, "Login uspješan!");
                if (parentFrame != null) parentFrame.dispose();

                MainProjectForm projectForm = new MainProjectForm(username);
                JFrame newFrame = new JFrame("Glavni prozor projekta");
                newFrame.setContentPane(projectForm.getMainPanel());
                newFrame.setSize(800, 500);
                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                newFrame.setLocationRelativeTo(null);
                newFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Pogrešni podaci!");
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 35));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setParentFrame(JFrame frame) {
        this.parentFrame = frame;
    }
}
