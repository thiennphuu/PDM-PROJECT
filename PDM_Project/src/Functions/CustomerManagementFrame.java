package Functions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CustomerManagementFrame extends JFrame {

    private JTable customerTable;
    private DefaultTableModel customerTableModel;

    public CustomerManagementFrame() {
        setTitle("Customer Management System - Manage Customers");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Create a label
        JLabel titleLabel = new JLabel("Manage Customers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create customer table
        String[] customerColumns = {
                "Customer_ID", "First_Name", "Last_Name", "Customer_Name", "Phone_Number", "Address"
        };
        customerTableModel = new DefaultTableModel(customerColumns, 0);
        customerTable = new JTable(customerTableModel);
        JScrollPane customerScrollPane = new JScrollPane(customerTable);

        // Add table to panel
        mainPanel.add(customerScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Buttons
        JButton loadCustomersButton = new JButton("Load Customers");
        JButton viewOrdersButton = new JButton("View Orders");

        buttonPanel.add(loadCustomersButton);
        buttonPanel.add(viewOrdersButton);

        // Add button functionality
        loadCustomersButton.addActionListener(e -> loadCustomerData());

        viewOrdersButton.addActionListener(e -> viewCustomerOrders());

        setVisible(true);
    }

    // Load customer data from the database
    private void loadCustomerData() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Customer")) {

            customerTableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                customerTableModel.addRow(new Object[]{
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("First_Name"),
                        resultSet.getString("Last_Name"),
                        resultSet.getString("Customer_Name"),
                        resultSet.getString("Phone_Number"),
                        resultSet.getString("Address")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }

// View orders for the selected customer
private void viewCustomerOrders() {
    int selectedRow = customerTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a customer to view orders.");
        return;
    }

    int customerId = Integer.parseInt(customerTableModel.getValueAt(selectedRow, 0).toString());

    // Create a dialog to display orders
    JDialog orderDialog = new JDialog(this, "Orders for Customer ID: " + customerId, true);
    orderDialog.setSize(1000, 500);
    orderDialog.setLocationRelativeTo(this);

    String[] orderColumns = {"Order_ID", "Order_Date", "Expected_Delivery", "Product_Name", "Category", "Price", "Supplier_ID", "Description"};
    DefaultTableModel orderTableModel = new DefaultTableModel(orderColumns, 0);
    JTable orderTable = new JTable(orderTableModel);
    JScrollPane orderScrollPane = new JScrollPane(orderTable);

    orderDialog.add(orderScrollPane, BorderLayout.CENTER);

    // Load orders and product details from the database
    String query = """
        SELECT 
            o.Order_ID,
            o.Order_Date,
            o.Expected_Delivery,
            p.Product_Name,
            p.Category,
            p.Price,
            p.Supplier_ID,
            p.Description
        FROM Orders o
        JOIN Order_Detail od ON o.Order_ID = od.Order_ID
        JOIN Product p ON od.Product_ID = p.Product_ID
        WHERE o.Customer_ID = ?
    """;

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, customerId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int orderId = resultSet.getInt("Order_ID");
            Date orderDate = resultSet.getDate("Order_Date");
            Date expectedDelivery = resultSet.getDate("Expected_Delivery");
            String productName = resultSet.getString("Product_Name");
            String category = resultSet.getString("Category");
            double price = resultSet.getDouble("Price");
            int supplierId = resultSet.getInt("Supplier_ID");
            String description = resultSet.getString("Description");

            // Add order and product data to the order table model
            orderTableModel.addRow(new Object[]{orderId, orderDate, expectedDelivery, productName, category, price, supplierId, description});
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage());
    }

    orderDialog.setVisible(true);
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerManagementFrame manageCustomerFrame = new CustomerManagementFrame();
            manageCustomerFrame.setVisible(true);
        });
    }
}
