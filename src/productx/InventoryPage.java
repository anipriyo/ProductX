/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productx;

/**
 *
 * @author annae
 */

//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.*;

//public class InventoryPage {
//    private JFrame frame;
//    private String cid;
//    private Connection con;
//    private JPanel inventoryPanel;
//
//    public InventoryPage(String cid, Connection con) {
//        this.cid = cid;
//        this.con = con;
//        initializeUI();
//    }
//
//    private void initializeUI() {
//        frame = new JFrame("My Inventory");
//        frame.setSize(600, 500);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//
//        inventoryPanel = new JPanel();
//        inventoryPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Single column layout
//        JScrollPane scrollPane = new JScrollPane(inventoryPanel);
//
//        JButton btnAddProduct = new JButton("Add Product");
//        btnAddProduct.addActionListener(e -> addProduct());
//
//        frame.add(scrollPane, BorderLayout.CENTER);
//        frame.add(btnAddProduct, BorderLayout.SOUTH);
//        
//        loadInventory();
//        frame.setVisible(true);
//    }
//
//    private void loadInventory() {
//        try {
//            String query = "SELECT PID, Product_Name, Price, Description, Quantity FROM inventory WHERE CID = ?";
//            PreparedStatement pst = con.prepareStatement(query);
//            pst.setString(1, cid);
//            ResultSet rs = pst.executeQuery();
//
//            inventoryPanel.removeAll();
//            while (rs.next()) {
//                int pid = rs.getInt("PID");
//                String name = rs.getString("Product_Name");
//                double price = rs.getDouble("Price");
//                String description = rs.getString("Description");
//                int quantity = rs.getInt("Quantity");
//
//                JPanel productPanel = new JPanel();
//                productPanel.setLayout(new BorderLayout());
//                productPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
//                
//                JLabel lblProduct = new JLabel(name + " - ₹" + price + " | Qty: " + quantity);
//                JButton btnEdit = new JButton("Edit");
//                JButton btnDelete = new JButton("Delete");
//                
//                btnEdit.addActionListener(e -> editProduct(pid, name, price, description, quantity));
//                btnDelete.addActionListener(e -> deleteProduct(pid));
//
//                JPanel buttonPanel = new JPanel();
//                buttonPanel.add(btnEdit);
//                buttonPanel.add(btnDelete);
//
//                productPanel.add(lblProduct, BorderLayout.CENTER);
//                productPanel.add(buttonPanel, BorderLayout.EAST);
//                
//                inventoryPanel.add(productPanel);
//            }
//            inventoryPanel.revalidate();
//            inventoryPanel.repaint();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void addProduct() {
//        new AddProduct(cid,con); 
//    }
//
//    private void editProduct(int pid, String name, double price, String desc, int qty) {
//        JTextField nameField = new JTextField(name);
//        JTextField priceField = new JTextField(String.valueOf(price));
//        JTextField descField = new JTextField(desc);
//        JTextField qtyField = new JTextField(String.valueOf(qty));
//
//        Object[] fields = {
//            "Product Name:", nameField,
//            "Price:", priceField,
//            "Description:", descField,
//            "Quantity:", qtyField
//        };
//
//        int result = JOptionPane.showConfirmDialog(frame, fields, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
//        if (result == JOptionPane.OK_OPTION) {
//            try {
//                String query = "UPDATE inventory SET Product_Name = ?, Price = ?, Description = ?, Quantity = ? WHERE PID = ? AND CID = ?";
//                PreparedStatement pst = con.prepareStatement(query);
//                pst.setString(1, nameField.getText());
//                pst.setDouble(2, Double.parseDouble(priceField.getText()));
//                pst.setString(3, descField.getText());
//                pst.setInt(4, Integer.parseInt(qtyField.getText()));
//                pst.setInt(5, pid);
//                pst.setString(6, cid);
//                pst.executeUpdate();
//                loadInventory();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void deleteProduct(int pid) {
//        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this product?", "Delete", JOptionPane.YES_NO_OPTION);
//        if (confirm == JOptionPane.YES_OPTION) {
//            try {
//                String query = "DELETE FROM inventory WHERE PID = ? AND CID = ?";
//                PreparedStatement pst = con.prepareStatement(query);
//                pst.setInt(1, pid);
//                pst.setString(2, cid);
//                pst.executeUpdate();
//                loadInventory();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InventoryPage {
    private JFrame frame;
    private String cid;
    private Connection con;
    private JPanel inventoryPanel;
    private final int CARD_WIDTH = 550;
    private final int CARD_HEIGHT = 130;
    private final Color CARD_BACKGROUND = new Color(255, 255, 255);
    private final Color CARD_BORDER = new Color(220, 220, 220);
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public InventoryPage(String cid, Connection con) {
        this.cid = cid;
        this.con = con;
        initializeUI();
    }

    private void initializeUI() {
        try {
            // Set system look and feel for better UI
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Product Inventory Management");
        frame.setSize(900, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().setBackground(new Color(245, 247, 250));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Main Content Panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 247, 250));
        
        // Create a panel for inventory with a proper layout for cards
        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inventoryPanel.setBackground(Color.WHITE);
        
        // Wrap inventory panel in a scroll pane
        JScrollPane scrollPane = new JScrollPane(inventoryPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Color.WHITE);

        // Stats Panel (above inventory)
        JPanel statsPanel = createStatsPanel();
        
        // Button Panel (below inventory)
        JPanel buttonPanel = createButtonPanel();

        // Add components to content panel
        contentPanel.add(statsPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add panels to frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(contentPanel, BorderLayout.CENTER);
        
        // Set frame properties
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Load inventory data
        loadInventory();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        // Title on the left
        JLabel titleLabel = new JLabel("My Inventory");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        
        // Back button on the right
        JButton backButton = new JButton("← Back");
        backButton.setFont(REGULAR_FONT);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(SECONDARY_COLOR);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> frame.dispose());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);
        
        // Stats will be populated with real data
        int totalProducts = 0;
        double totalValue = 0;
        int lowStockItems = 0;
        
        try {
            // Count total products
            String countQuery = "SELECT COUNT(*) FROM inventory WHERE CID = ?";
            PreparedStatement countPst = con.prepareStatement(countQuery);
            countPst.setString(1, cid);
            ResultSet countRs = countPst.executeQuery();
            if (countRs.next()) {
                totalProducts = countRs.getInt(1);
            }
            
            // Calculate total inventory value
            String valueQuery = "SELECT SUM(Price * Quantity) FROM inventory WHERE CID = ?";
            PreparedStatement valuePst = con.prepareStatement(valueQuery);
            valuePst.setString(1, cid);
            ResultSet valueRs = valuePst.executeQuery();
            if (valueRs.next()) {
                totalValue = valueRs.getDouble(1);
            }
            
            // Count low stock items (less than 5)
            String lowStockQuery = "SELECT COUNT(*) FROM inventory WHERE CID = ? AND Quantity < 5";
            PreparedStatement lowStockPst = con.prepareStatement(lowStockQuery);
            lowStockPst.setString(1, cid);
            ResultSet lowStockRs = lowStockPst.executeQuery();
            if (lowStockRs.next()) {
                lowStockItems = lowStockRs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Create stat cards
        statsPanel.add(createStatCard("Total Products", totalProducts + " items", PRIMARY_COLOR));
        statsPanel.add(createStatCard("Inventory Value", "₹" + String.format("%.2f", totalValue), SUCCESS_COLOR));
        statsPanel.add(createStatCard("Low Stock Items", lowStockItems + " products", WARNING_COLOR));
        
        return statsPanel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 1, true),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SMALL_FONT);
        titleLabel.setForeground(Color.GRAY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(SUBTITLE_FONT);
        valueLabel.setForeground(color);
        
        cardPanel.add(titleLabel, BorderLayout.NORTH);
        cardPanel.add(valueLabel, BorderLayout.CENTER);
        
        return cardPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton, PRIMARY_COLOR);
        refreshButton.addActionListener(e -> loadInventory());
        
        JButton addButton = new JButton("+ Add Product");
        styleButton(addButton, SUCCESS_COLOR);
        addButton.addActionListener(e -> addProduct());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(addButton);
        
        return buttonPanel;
    }
    
    private void styleButton(JButton button, Color color) {
        button.setFont(REGULAR_FONT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
    }

    private void loadInventory() {
        try {
            String query = "SELECT PID, Product_Name, Price, Description, Quantity FROM inventory WHERE CID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, cid);
            ResultSet rs = pst.executeQuery();

            inventoryPanel.removeAll();
            
            // Add spacing at the top
            inventoryPanel.add(Box.createVerticalStrut(5));
            
            boolean hasProducts = false;
            while (rs.next()) {
                hasProducts = true;
                int pid = rs.getInt("PID");
                String name = rs.getString("Product_Name");
                double price = rs.getDouble("Price");
                String description = rs.getString("Description");
                int quantity = rs.getInt("Quantity");

                JPanel productCard = createProductCard(pid, name, price, description, quantity);
                inventoryPanel.add(productCard);
                
                // Add spacing between cards
                inventoryPanel.add(Box.createVerticalStrut(15));
            }
            
            // Show message if no products found
            if (!hasProducts) {
                JPanel emptyPanel = new JPanel(new BorderLayout());
                emptyPanel.setBackground(Color.WHITE);
                emptyPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
                
                JLabel emptyLabel = new JLabel("No products in your inventory. Click 'Add Product' to get started.", JLabel.CENTER);
                emptyLabel.setFont(REGULAR_FONT);
                emptyLabel.setForeground(Color.GRAY);
                
                emptyPanel.add(emptyLabel, BorderLayout.CENTER);
                inventoryPanel.add(emptyPanel);
            }
            
            // Add extra space at the bottom for better scrolling
            inventoryPanel.add(Box.createVerticalStrut(10));
            
            inventoryPanel.revalidate();
            inventoryPanel.repaint();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error loading inventory: " + e.getMessage(), 
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private JPanel createProductCard(int pid, String name, double price, String description, int quantity) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(15, 10));
        cardPanel.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        cardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, CARD_HEIGHT));
        cardPanel.setBackground(CARD_BACKGROUND);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CARD_BORDER, 1, true),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Left side - product image placeholder (in a real app, this would be an actual product image)
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(100, 100));
        imagePanel.setBackground(new Color(240, 240, 240));
        imagePanel.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 1));
        imagePanel.setLayout(new BorderLayout());
        
        JLabel imageLabel = new JLabel("Product", JLabel.CENTER);
        imageLabel.setForeground(Color.GRAY);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        // Middle - product details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(CARD_BACKGROUND);
        
        // Product name with larger font
        JLabel lblName = new JLabel(name);
        lblName.setFont(SUBTITLE_FONT);
        lblName.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Price label with custom color
        JLabel lblPrice = new JLabel("₹" + String.format("%.2f", price));
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPrice.setForeground(PRIMARY_COLOR);
        lblPrice.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Description with smaller font
        JLabel lblDesc = new JLabel("<html><body width='300'>" + 
            (description.length() > 100 ? description.substring(0, 97) + "..." : description) + 
            "</body></html>");
        lblDesc.setFont(SMALL_FONT);
        lblDesc.setForeground(new Color(100, 100, 100));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Stock status indicator
        JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        stockPanel.setBackground(CARD_BACKGROUND);
        stockPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel stockLabel = new JLabel("Stock Status: ");
        stockLabel.setFont(SMALL_FONT);
        
        JLabel stockStatus = new JLabel(String.valueOf(quantity));
        stockStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        if (quantity > 10) {
            stockStatus.setText("In Stock (" + quantity + ")");
            stockStatus.setForeground(SUCCESS_COLOR);
        } else if (quantity > 0) {
            stockStatus.setText("Low Stock (" + quantity + ")");
            stockStatus.setForeground(WARNING_COLOR);
        } else {
            stockStatus.setText("Out of Stock");
            stockStatus.setForeground(DANGER_COLOR);
        }
        
        stockPanel.add(stockLabel);
        stockPanel.add(stockStatus);
        
        detailsPanel.add(lblName);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(lblPrice);
        detailsPanel.add(Box.createVerticalStrut(5));
        detailsPanel.add(lblDesc);
        detailsPanel.add(Box.createVerticalStrut(8));
        detailsPanel.add(stockPanel);
        
        // Right side - Action buttons panel
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(CARD_BACKGROUND);
        
        JButton btnEdit = createActionButton("Edit", SECONDARY_COLOR);
        btnEdit.addActionListener(e -> editProduct(pid, name, price, description, quantity));
        
        JButton btnDelete = createActionButton("Delete", DANGER_COLOR);
        btnDelete.addActionListener(e -> deleteProduct(pid));
        
        actionPanel.add(btnEdit);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnDelete);
        
        // Add all components to card
        cardPanel.add(imagePanel, BorderLayout.WEST);
        cardPanel.add(detailsPanel, BorderLayout.CENTER);
        cardPanel.add(actionPanel, BorderLayout.EAST);
        
        // Add hover effect
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                    BorderFactory.createEmptyBorder(15, 20, 15, 20)
                ));
            }
        });
        
        return cardPanel;
    }
    
    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(90, 35));
        button.setFont(REGULAR_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private void addProduct() {
        new AddProduct(cid, con); 
        // Refresh inventory after adding a new product
        loadInventory();
    }

    private void editProduct(int pid, String name, double price, String desc, int qty) {
        JTextField nameField = new JTextField(name);
        nameField.setFont(REGULAR_FONT);
        
        JTextField priceField = new JTextField(String.valueOf(price));
        priceField.setFont(REGULAR_FONT);
        
        JTextArea descField = new JTextArea(desc, 4, 25);
        descField.setFont(REGULAR_FONT);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descField);
        
        JTextField qtyField = new JTextField(String.valueOf(qty));
        qtyField.setFont(REGULAR_FONT);

        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        inputPanel.add(new JLabel("Product Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Price (₹):"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descScroll);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(qtyField);

        JDialog dialog = new JDialog(frame, "Edit Product", true);
        dialog.setLayout(new BorderLayout());
        dialog.add(inputPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(REGULAR_FONT);
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JButton saveButton = new JButton("Save Changes");
        saveButton.setFont(REGULAR_FONT);
        saveButton.setBackground(SUCCESS_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            try {
                // Validate inputs
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Product name cannot be empty!", 
                                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double newPrice;
                int newQty;
                try {
                    newPrice = Double.parseDouble(priceField.getText());
                    if (newPrice < 0) {
                        JOptionPane.showMessageDialog(dialog, "Price cannot be negative!", 
                                                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid price format!", 
                                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    newQty = Integer.parseInt(qtyField.getText());
                    if (newQty < 0) {
                        JOptionPane.showMessageDialog(dialog, "Quantity cannot be negative!", 
                                                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid quantity format!", 
                                                "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Update the product
                String query = "UPDATE inventory SET Product_Name = ?, Price = ?, Description = ?, Quantity = ? WHERE PID = ? AND CID = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, nameField.getText().trim());
                pst.setDouble(2, newPrice);
                pst.setString(3, descField.getText().trim());
                pst.setInt(4, newQty);
                pst.setInt(5, pid);
                pst.setString(6, cid);
                
                int updated = pst.executeUpdate();
                if (updated > 0) {
                    dialog.dispose();
                    loadInventory();
                    
                    // Show success notification
                    showNotification("Product updated successfully!", SUCCESS_COLOR);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update product!", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error: " + ex.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private void deleteProduct(int pid) {
        JDialog confirmDialog = new JDialog(frame, "Confirm Delete", true);
        confirmDialog.setLayout(new BorderLayout());
        
        JPanel messagePanel = new JPanel(new BorderLayout(10, 10));
        messagePanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        JLabel messageLabel = new JLabel("<html>Are you sure you want to delete this product?<br>This action cannot be undone.</html>");
        messageLabel.setFont(REGULAR_FONT);
        
        messagePanel.add(iconLabel, BorderLayout.WEST);
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(REGULAR_FONT);
        cancelButton.addActionListener(e -> confirmDialog.dispose());
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(REGULAR_FONT);
        deleteButton.setBackground(DANGER_COLOR);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> {
            try {
                String query = "DELETE FROM inventory WHERE PID = ? AND CID = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, pid);
                pst.setString(2, cid);
                
                int deleted = pst.executeUpdate();
                confirmDialog.dispose();
                
                if (deleted > 0) {
                    loadInventory();
                    showNotification("Product deleted successfully!", DANGER_COLOR);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to delete product!", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);
        
        confirmDialog.add(messagePanel, BorderLayout.CENTER);
        confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        confirmDialog.pack();
        confirmDialog.setLocationRelativeTo(frame);
        confirmDialog.setResizable(false);
        confirmDialog.setVisible(true);
    }
    
    private void showNotification(String message, Color color) {
        JDialog notification = new JDialog(frame, false);
        notification.setUndecorated(true);
        notification.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(color.darker(), 1));
        
        JLabel label = new JLabel(message);
        label.setFont(REGULAR_FONT);
        label.setForeground(Color.WHITE);
        label.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        panel.add(label, BorderLayout.CENTER);
        notification.add(panel);
        
        notification.pack();
        notification.setLocationRelativeTo(null);
        
        // Position at the bottom right corner of the main frame
        Point frameLoc = frame.getLocationOnScreen();
        notification.setLocation(
            frameLoc.x + frame.getWidth() - notification.getWidth() - 20,
            frameLoc.y + frame.getHeight() - notification.getHeight() - 20
        );
        
        notification.setVisible(true);
        
        // Auto-hide after 3 seconds
        new Timer(3000, e -> {
            notification.dispose();
        }).start();
    }
    
    // For testing the UI independently
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            // This is just for testing - replace with actual DB connection in production
            Connection dummyConnection = null;
            new InventoryPage("1", dummyConnection);
        });
    }
}