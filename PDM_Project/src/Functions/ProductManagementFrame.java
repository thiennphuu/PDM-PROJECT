package Functions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ProductManagementFrame extends JFrame {

    private JTable productTable;
    private DefaultTableModel productTableModel;

    public ProductManagementFrame() {
        setTitle("Product Management System - Manage Products");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Create a label
        JLabel titleLabel = new JLabel("Manage Products", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Create product table
        String[] productColumns = {
                "Product_ID", "Product_Name", "Category", "Price", "Supplier_ID", "Description"
        };
        productTableModel = new DefaultTableModel(productColumns, 0);
        productTable = new JTable(productTableModel);
        JScrollPane productScrollPane = new JScrollPane(productTable);

        // Add table to panel
        mainPanel.add(productScrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Buttons
        JButton loadProductsButton = new JButton("Load Products");
        JButton viewSuppliersButton = new JButton("View Supplier");
        JButton viewWarehouseButton = new JButton("View Warehouse");
        JButton viewShipmentStatusButton = new JButton("View Shipment Status");

        buttonPanel.add(loadProductsButton);
        buttonPanel.add(viewSuppliersButton);
        buttonPanel.add(viewWarehouseButton);
        buttonPanel.add(viewShipmentStatusButton);

        // Add button functionality
        loadProductsButton.addActionListener(e -> loadProductData());
        viewSuppliersButton.addActionListener(e -> viewSupplierForSelectedProduct());
        viewWarehouseButton.addActionListener(e -> viewWarehouseForSelectedProduct());
        viewShipmentStatusButton.addActionListener(e -> viewShipmentStatusForSelectedProduct());

        setVisible(true);
    }

    // Load product data from the database
    private void loadProductData() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Product")) {

            productTableModel.setRowCount(0); // Clear existing rows
            while (resultSet.next()) {
                productTableModel.addRow(new Object[]{
                        resultSet.getInt("Product_ID"),
                        resultSet.getString("Product_Name"),
                        resultSet.getString("Category"),
                        resultSet.getBigDecimal("Price"),
                        resultSet.getInt("Supplier_ID"),
                        resultSet.getString("Description")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading product data: " + e.getMessage());
        }
    }

    // View supplier for the selected product
    private void viewSupplierForSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to view its supplier.");
            return;
        }

        int supplierId = (int) productTableModel.getValueAt(selectedRow, 4); // Supplier_ID column
        String query = "SELECT Supplier_ID, Supplier_Name, Contact_Number, Address FROM Supplier WHERE Supplier_ID = ?";
        showDataDialog("Supplier", query, new String[]{"Supplier_ID", "Supplier_Name", "Contact_Number", "Address"}, supplierId);
    }

    private void viewWarehouseForSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to view its warehouse.");
            return;
        }
    
        int productId = (int) productTableModel.getValueAt(selectedRow, 0); // Product_ID column
    
        String query = """
            SELECT 
                w.Warehouse_ID, 
                w.Warehouse_Name, 
                w.Location, 
                w.Capacity
            FROM Warehouse w
            JOIN Store s ON w.Warehouse_ID = s.Warehouse_ID
            WHERE s.Product_ID = ?
        """;
    
        showDataDialog(
            "Warehouse Details",
            query,
            new String[]{"Warehouse_ID", "Warehouse_Name", "Location", "Capacity"},
            productId
        );
    }
    


// View shipment status for the selected product
private void viewShipmentStatusForSelectedProduct() {
    int selectedRow = productTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a product to view its shipment status.");
        return;
    }

    int productId = (int) productTableModel.getValueAt(selectedRow, 0); // Product_ID column
    String query = """
        SELECT 
            o.Order_ID,
            s.Delivery_Status, 
            s.Recipent_Date, 
            s.Delivery_Date
        FROM Orders o
        JOIN Order_Detail od ON o.Order_ID = od.Order_ID
        JOIN Shipment s ON o.Shipment_ID = s.Shipment_ID
        WHERE od.Product_ID = ?
    """;
    showDataDialog(
        "Shipment Status",
        query,
        new String[]{"Order_ID", "Delivery_Status", "Recipent_Date", "Delivery_Date"},
        productId
    );
}


    // Helper method to display data in a new dialog
    private void showDataDialog(String title, String query, String[] columnNames, int parameter) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, parameter);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Object[] rowData = new Object[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    rowData[i] = resultSet.getObject(columnNames[i]);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductManagementFrame());
    }
}
