/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productx;

//import java.awt.*;
//import java.awt.event.*;
//import java.sql.*;
//import java.util.Random;
//import javax.swing.*;
//
//public class AddProduct {
//    private JFrame frame;
//    private JTextField nameField, priceField, descField, cidField, quantityField;
//    private JButton addButton;
//    private String cid;
//    private Connection conn;
//
//    public AddProduct(String cid, Connection conn) {
//        this.cid = cid;
//        this.conn = conn;
//        initializeUI(); // If there's a UI setup method, call it
//    }
//    
//    private void initializeUI() {
//        frame = new JFrame("Inventory Management");
//        frame.setSize(400, 400);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        
//        JPanel panel = new JPanel();
//        panel.setLayout(new GridLayout(6, 2));
//        
//        panel.add(new JLabel("Product Name:"));
//        nameField = new JTextField();
//        panel.add(nameField);
//        
//        panel.add(new JLabel("Price:"));
//        priceField = new JTextField();
//        panel.add(priceField);
//        
//        panel.add(new JLabel("Description:"));
//        descField = new JTextField();
//        panel.add(descField);
//        
//        panel.add(new JLabel("CID:"));
//        cidField = new JTextField();
//        panel.add(cidField);
//        
//        panel.add(new JLabel("Quantity:"));
//        quantityField = new JTextField();
//        panel.add(quantityField);
//        
//        addButton = new JButton("Add Product");
//        panel.add(addButton);
//        
//
//        addButton.addActionListener(e -> addProduct());
//        
//        frame.add(panel);
//        frame.setVisible(true);
//    }
//    
//        
//    public void addProduct() {
//    String name = nameField.getText().trim();
//    String price = priceField.getText().trim();
//    String desc = descField.getText().trim();
//    String cid = cidField.getText().trim();
//    String quantity = quantityField.getText().trim();
//    String image = null; // Update this if you handle image paths
//
//    try (Connection conn = DBConnect.getConnection()) {
//
//        // Step 1: Get the highest existing PID
//        String getMaxPidQuery = "SELECT MAX(CAST(PID AS UNSIGNED)) FROM inventory";
//        try (PreparedStatement maxPidStmt = conn.prepareStatement(getMaxPidQuery);
//             ResultSet rs = maxPidStmt.executeQuery()) {
//
//            long newPID = 10000001; // Default starting PID
//
//            if (rs.next() && rs.getObject(1) != null) {
//                newPID = rs.getLong(1) + 1; // Increment highest PID
//            }
//
//            // Step 2: Insert new product
//            String insertQuery = "INSERT INTO inventory (PID, Product_Name, Price, Description, Image, CID, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
//                insertStmt.setString(1, String.valueOf(newPID)); // Set generated PID
//                insertStmt.setString(2, name);
//                insertStmt.setDouble(3, Double.parseDouble(price));
//                insertStmt.setString(4, desc);
//                insertStmt.setString(5, image);
//                insertStmt.setString(6, cid);
//                insertStmt.setInt(7, Integer.parseInt(quantity));
//
//                int rowsInserted = insertStmt.executeUpdate();
//                if (rowsInserted > 0) {
//                    JOptionPane.showMessageDialog(null, "Product added successfully with PID: " + newPID);
//                }
//            }
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
//    } catch (NumberFormatException e) {
//        JOptionPane.showMessageDialog(null, "Invalid Price or Quantity format. Please enter valid numbers.");
//    }
//}
//
//
//    private int getExistingOrNewPID(String productName) {
//        try (Connection conn = DBConnect.getConnection();
//             PreparedStatement stmt = conn.prepareStatement("SELECT PID FROM inventory WHERE LOWER(Product_Name) = LOWER(?) LIMIT 1")
//        ) {
//            stmt.setString(1, productName);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                return rs.getInt("PID"); // Return existing PID if found
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        
//        // Generate a unique 8-digit PID
//        return generateUniquePID();
//    }
//    
//    private int generateUniquePID() {
//        Random rand = new Random();
//        int newPID;
//        
//        while (true) {
//            newPID = 10000000 + rand.nextInt(90000000); // Generate an 8-digit PID
//            
//            try (Connection conn = DBConnect.getConnection();
//                 PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM inventory WHERE PID = ?")
//            ) {
//                stmt.setInt(1, newPID);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next() && rs.getInt(1) == 0) {
//                    return newPID; // Return if unique
//                }
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//}


import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Random;

public class AddProduct {
    private JFrame frame;
    private JTextField nameField, priceField, descField, cidField, quantityField;
    private JButton addButton, clearButton, closeButton;
    private String cid;
    private Connection conn;
    private JPanel mainPanel;
    
    // Colors and fonts for consistent UI
    private final Color PRIMARY_COLOR = new Color(0, 102, 204);
    private final Color SECONDARY_COLOR = new Color(240, 240, 240);
    private final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    private final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    private final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public AddProduct(String cid, Connection conn) {
        this.cid = cid;
        this.conn = conn;
        initializeUI();
    }
    
    private void initializeUI() {
        // Set up the frame
        frame = new JFrame("Add New Product");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center on screen
        
        // Create main panel with padding
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Add New Product to Inventory");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Product Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setFont(LABEL_FONT);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(nameField, gbc);
        
        // Price
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel priceLabel = new JLabel("Price ($):");
        priceLabel.setFont(LABEL_FONT);
        formPanel.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        priceField = new JTextField(20);
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(priceField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(LABEL_FONT);
        formPanel.add(descLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descField = new JTextField(20);
        descField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(descField, gbc);
        
        // CID
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        JLabel cidLabel = new JLabel("Category ID:");
        cidLabel.setFont(LABEL_FONT);
        formPanel.add(cidLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cidField = new JTextField(20);
        cidField.setFont(new Font("Arial", Font.PLAIN, 14));
        if (cid != null && !cid.isEmpty()) {
            cidField.setText(cid);
            cidField.setEditable(false);
        }
        formPanel.add(cidField, gbc);
        
        // Quantity
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(LABEL_FONT);
        formPanel.add(quantityLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        quantityField = new JTextField(20);
        quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(quantityField, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Add Product Button
        addButton = createStyledButton("Add Product", PRIMARY_COLOR, Color.WHITE);
        addButton.addActionListener(e -> addProduct());
        panel.add(addButton);
        
        // Clear Fields Button
        clearButton = createStyledButton("Clear Fields", SECONDARY_COLOR, Color.BLACK);
        clearButton.addActionListener(e -> clearFields());
        panel.add(clearButton);
        
        // Close Button
        closeButton = createStyledButton("Close", new Color(220, 53, 69), Color.WHITE);
        closeButton.addActionListener(e -> frame.dispose());
        panel.add(closeButton);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        descField.setText("");
        if (cid == null || cid.isEmpty()) {
            cidField.setText("");
        }
        quantityField.setText("");
        nameField.requestFocus();
    }
    
    public void addProduct() {
        // Validate input fields
        if (!validateFields()) {
            return;
        }
        
        String name = nameField.getText().trim();
        double price = Double.parseDouble(priceField.getText().trim());
        String desc = descField.getText().trim();
        String categoryId = cidField.getText().trim();
        int quantity = Integer.parseInt(quantityField.getText().trim());
        String image = null; // Update this if you handle image paths

        try {
            Connection connection = (conn != null) ? conn : DBConnect.getConnection();
            
            // Get the highest existing PID
            String getMaxPidQuery = "SELECT MAX(CAST(PID AS UNSIGNED)) FROM inventory";
            try (PreparedStatement maxPidStmt = connection.prepareStatement(getMaxPidQuery);
                 ResultSet rs = maxPidStmt.executeQuery()) {
                
                long newPID = 10000001; // Default starting PID
                
                if (rs.next() && rs.getObject(1) != null) {
                    newPID = rs.getLong(1) + 1; // Increment highest PID
                }
                
                // Insert new product
                String insertQuery = "INSERT INTO inventory (PID, Product_Name, Price, Description, Image, CID, Quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, String.valueOf(newPID));
                    insertStmt.setString(2, name);
                    insertStmt.setDouble(3, price);
                    insertStmt.setString(4, desc);
                    insertStmt.setString(5, image);
                    insertStmt.setString(6, categoryId);
                    insertStmt.setInt(7, quantity);
                    
                    int rowsInserted = insertStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        showSuccessDialog("Product added successfully with PID: " + newPID);
                        clearFields();
                    }
                }
            }
            
            // Only close connection if we created a new one
            if (conn == null) {
                connection.close();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid Price or Quantity format. Please enter valid numbers.");
        }
    }
    
    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();
        
        if (nameField.getText().trim().isEmpty()) {
            errors.append("• Product Name is required\n");
        }
        
        try {
            if (priceField.getText().trim().isEmpty()) {
                errors.append("• Price is required\n");
            } else {
                double price = Double.parseDouble(priceField.getText().trim());
                if (price < 0) {
                    errors.append("• Price cannot be negative\n");
                }
            }
        } catch (NumberFormatException e) {
            errors.append("• Price must be a valid number\n");
        }
        
        if (descField.getText().trim().isEmpty()) {
            errors.append("• Description is required\n");
        }
        
        if (cidField.getText().trim().isEmpty()) {
            errors.append("• Category ID is required\n");
        }
        
        try {
            if (quantityField.getText().trim().isEmpty()) {
                errors.append("• Quantity is required\n");
            } else {
                int quantity = Integer.parseInt(quantityField.getText().trim());
                if (quantity < 0) {
                    errors.append("• Quantity cannot be negative\n");
                }
            }
        } catch (NumberFormatException e) {
            errors.append("• Quantity must be a valid integer\n");
        }
        
        if (errors.length() > 0) {
            showErrorDialog("Please correct the following errors:\n" + errors.toString());
            return false;
        }
        
        return true;
    }
    
    private void showSuccessDialog(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog("Success");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
    
    private void showErrorDialog(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = optionPane.createDialog("Error");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }
}