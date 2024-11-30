import java.awt.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import Functions.ManageUserFrame;
import Functions.ProductManagementFrame;
import Functions.CustomerManagementFrame;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        // Set up JFrame properties
        setTitle("Warehouse Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600); // Adjusted frame size to fit larger buttons
        setLayout(new BorderLayout());

        // Load background image
        BackgroundPanel backgroundPanel = new BackgroundPanel("Images/BackGround_warehouse.jpeg");
        backgroundPanel.setLayout(new BorderLayout()); // Set layout for the background panel

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 100, 100)); // 1 row, 4 columns, spacing between buttons
        buttonPanel.setOpaque(false); // Make the panel transparent

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(200, 100, 200, 100)); // Add padding around buttons

        // Create buttons with icons and labels
        JButton userButton = createButton("Worker", "Images/Worker_Icon.png");
        JButton productButton = createButton("Product", "Images/Product_Icon.png");
        JButton customerButton = createButton("Customer", "Images/Customer_Icon.png");
        JButton exitButton = createButton("Exit", "Images/Exit_Icon.png");

        // Add buttons to the panel
        buttonPanel.add(userButton);
        buttonPanel.add(productButton);
        buttonPanel.add(customerButton);
        buttonPanel.add(exitButton);

        // Add button panel to the background panel
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add the background panel to the frame
        add(backgroundPanel);

        // Center the frame on screen
        setLocationRelativeTo(null);

        // Add action listeners to the buttons
        userButton.addActionListener(e -> {
            ManageUserFrame manageUserFrame = new ManageUserFrame();
            manageUserFrame.setVisible(true);
        });

        productButton.addActionListener(e -> {
            ProductManagementFrame productManagementFrame = new ProductManagementFrame();
            productManagementFrame.setVisible(true);
        });

        customerButton.addActionListener(e -> {
            CustomerManagementFrame customerManagementFrame = new CustomerManagementFrame();
            customerManagementFrame.setVisible(true);
        });

        // Action listener for Exit button
        exitButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                System.exit(0); // Exit program
            }
        });
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);

        // Set icon
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledIcon = icon.getImage().getScaledInstance(60, 40, Image.SCALE_SMOOTH); // Adjusted size
        button.setIcon(new ImageIcon(scaledIcon));

        // Adjust button size
        button.setPreferredSize(new Dimension(150, 80)); // Adjusted size

        return button;
    }

    // Custom JPanel to handle background image
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = ImageIO.read(new File(imagePath)); // Load background image
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Scale the image to fit the panel
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.setVisible(true);
        });
    }
}
