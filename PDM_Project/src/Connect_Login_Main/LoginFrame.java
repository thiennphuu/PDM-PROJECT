import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        // Set up JFrame properties
        setTitle("Warehouse Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600); // Adjusted JFrame size
        setLayout(new BorderLayout());

        // Left panel for the illustration
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BorderLayout());

        // Load and scale the image to a smaller size
        ImageIcon originalIcon = new ImageIcon("Images/login_theme.png"); // Replace with your image path
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH); // Adjust dimensions (smaller size)
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel illustration = new JLabel(scaledIcon); // Use the scaled image
        JLabel title = new JLabel("Warehouse Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        leftPanel.add(illustration, BorderLayout.CENTER);
        leftPanel.add(title, BorderLayout.SOUTH);

        // Right panel for login form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(5, 1, 10, 10));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("Welcome", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));

        JTextField usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton loginButton = new JButton("Login");
        JButton closeButton = new JButton("Close");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Check predefined credentials
                if (username.equals("admin") && password.equals("admin")) {
                    // JOptionPane.showMessageDialog(LoginFrame.this, "Login Successful!");
                    dispose(); // Close the login frame
                    DashboardFrame dashboard = new DashboardFrame();
                    dashboard.setVisible(true); // Open the dashboard
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid Credentials");
                }
            }
        });

        closeButton.addActionListener(e -> System.exit(0));

        rightPanel.add(welcomeLabel);
        rightPanel.add(usernameField);
        rightPanel.add(passwordField);
        rightPanel.add(loginButton);
        rightPanel.add(closeButton);

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Center the frame on screen
        setLocationRelativeTo(null);
    }

}
