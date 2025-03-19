/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productx;

/**
 *
 * @author annae
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckoutBuy extends JFrame {

    private Connection con;
    private String sellerCid;
    private String buyerCid;
    private String pid;
    private JPanel mainPanel;
    
    // For potential cart implementation
    private Map<String, ProductItem> productItems;
    
    private JTextField quantityField;
    private JTextField addressField;
    private JTextField phoneField;
    private JLabel totalPriceLabel;
    private double pricePerUnit;
    private String productName;
    private int availableQuantity;


    // Inner class to store product information for cart functionality
    private class ProductItem {
        String pid;
        String sellerCid;
        String productName;
        double price;
        int quantity;
        int availableQuantity;
        
        ProductItem(String pid, String sellerCid, String productName, double price, int quantity, int availableQuantity) {
            this.pid = pid;
            this.sellerCid = sellerCid;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.availableQuantity = availableQuantity;
        }
    }

    // Constructor for single product checkout
    public CheckoutBuy(String sellerCid, String buyerCid, String pid, Connection con) {
        this.con = con;
        this.sellerCid = sellerCid;
        this.buyerCid = buyerCid;
        this.pid = pid;
        this.productItems = new HashMap<>();
        
        // Load product details
        loadProductDetails();
        
        // Initialize UI
        initializeUI();
        
        this.setVisible(true);
    }
    
    // Constructor for cart checkout (for future implementation)
    public CheckoutBuy(String buyerCid, Map<String, ProductItem> productItems, Connection con) {
        this.con = con;
        this.buyerCid = buyerCid;
        this.productItems = productItems;
        
        // Initialize UI for cart
        initializeCartUI();
        
        this.setVisible(true);
    }
    
    private void loadProductDetails() {
        try {
            String query = "SELECT Product_Name, Price, Quantity FROM Inventory WHERE PID = ? AND CID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, pid);
            pst.setString(2, sellerCid);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                productName = rs.getString("Product_Name");
                pricePerUnit = rs.getDouble("Price");
                availableQuantity = rs.getInt("Quantity");
                
                // Add to product items map for consistent processing
                productItems.put(pid, new ProductItem(pid, sellerCid, productName, pricePerUnit, 1, availableQuantity));
            } else {
                JOptionPane.showMessageDialog(this, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading product details: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
    private void loadUserDetails() {
        try {
            String query = "SELECT Address, Phone_Number FROM Customer WHERE CID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, buyerCid);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String address = rs.getString("Address");
                String phone = rs.getString("Phone_Number");
                
                addressField.setText(address != null ? address : "");
                phoneField.setText(phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initializeUI() {
        setTitle("Checkout - ProductX");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Top panel with logo/title
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel("ProductX - Checkout");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel);
        
        // Product details panel
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 1, 10, 10));
        productPanel.setBorder(BorderFactory.createTitledBorder("Product Details"));
        
        JLabel nameLabel = new JLabel("Product: " + productName);
        JLabel priceLabel = new JLabel("Price: $" + String.format("%.2f", pricePerUnit));
        JLabel availableLabel = new JLabel("Available: " + availableQuantity);
        
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.add(new JLabel("Quantity: "));
        quantityField = new JTextField(5);
        quantityField.setText("1");
        quantityPanel.add(quantityField);
        
        totalPriceLabel = new JLabel("Total Price: $" + String.format("%.2f", pricePerUnit));
        
        productPanel.add(nameLabel);
        productPanel.add(priceLabel);
        productPanel.add(availableLabel);
        productPanel.add(quantityPanel);
        productPanel.add(totalPriceLabel);
        
        // Add listener to update total price when quantity changes
        quantityField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPrice();
            }
        });
        
        // Shipping information panel
        JPanel shippingPanel = new JPanel();
        shippingPanel.setLayout(new GridLayout(0, 2, 10, 10));
        shippingPanel.setBorder(BorderFactory.createTitledBorder("Shipping Information"));
        
        shippingPanel.add(new JLabel("Delivery Address:"));
        addressField = new JTextField(20);
        shippingPanel.add(addressField);
        
        shippingPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField(15);
        shippingPanel.add(phoneField);
        
        // Load user details
        loadUserDetails();
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        JButton confirmButton = new JButton("Confirm Purchase");
        JButton cancelButton = new JButton("Cancel");
        
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPurchase();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // Add all panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(productPanel, BorderLayout.NORTH);
        centerPanel.add(shippingPanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private void initializeCartUI() {
        // This would be implemented for future cart functionality
        // Similar to initializeUI but with a list of products instead of a single product
        setTitle("Cart Checkout - ProductX");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        
        // Implementation would be similar but with a table of products and quantities
        JOptionPane.showMessageDialog(this, "Cart functionality is not yet implemented.", 
                                     "Information", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
    
    private void updateTotalPrice() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0", 
                                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                quantityField.setText("1");
                quantity = 1;
            } else if (quantity > availableQuantity) {
                JOptionPane.showMessageDialog(this, "Quantity cannot exceed available stock (" + 
                                            availableQuantity + ")", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                quantityField.setText(String.valueOf(availableQuantity));
                quantity = availableQuantity;
            }
            
            double totalPrice = quantity * pricePerUnit;
            totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
            
            // Update quantity in product item
            productItems.get(pid).quantity = quantity;
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            quantityField.setText("1");
            updateTotalPrice();
        }
    }
    
    private void processPurchase() {
        // Validate input
        if (addressField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a delivery address", 
                                        "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a phone number", 
                                        "Missing Information", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            
            if (quantity <= 0 || quantity > availableQuantity) {
                updateTotalPrice(); // This will show appropriate error message
                return;
            }
            
            // Update customer information if needed
            updateCustomerInfo();
            
            // Insert into Buy_Record table
            boolean success = insertBuyRecord();
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Purchase successful! Thank you for shopping with ProductX.", 
                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for quantity", 
                                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCustomerInfo() {
        try {
            String query = "UPDATE Customer SET Address = ?, Phone_Number = ? WHERE CID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, addressField.getText().trim());
            pst.setString(2, phoneField.getText().trim());
            pst.setString(3, buyerCid);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Non-critical error, can proceed with purchase
            System.err.println("Could not update customer information: " + e.getMessage());
        }
    }
    
    private boolean insertBuyRecord() {
        boolean success = true;
        Connection transactionCon = null;
        
        try {
            // Disable auto-commit for transaction
            con.setAutoCommit(false);
            transactionCon = con;
            
            for (ProductItem item : productItems.values()) {
                // Check if quantity is still available (in case of concurrent purchases)
                String checkQuery = "SELECT Quantity FROM Inventory WHERE PID = ? AND CID = ?";
                PreparedStatement checkPst = transactionCon.prepareStatement(checkQuery);
                checkPst.setString(1, item.pid);
                checkPst.setString(2, item.sellerCid);
                ResultSet checkRs = checkPst.executeQuery();
                
                if (checkRs.next()) {
                    int currentQuantity = checkRs.getInt("Quantity");
                    
                    if (currentQuantity < item.quantity) {
                        JOptionPane.showMessageDialog(this, "Sorry, only " + currentQuantity + 
                                                    " units of " + item.productName + " are available now.", 
                                                    "Insufficient Stock", JOptionPane.ERROR_MESSAGE);
                        transactionCon.rollback();
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Product no longer available", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                    transactionCon.rollback();
                    return false;
                }
                
                // Insert into Buy_Record
                String insertQuery = "INSERT INTO Buy_Record (PID, Product_Name, Buying_Price, Buyer_CID, Seller_CID, Quantity) " +
                                    "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertPst = transactionCon.prepareStatement(insertQuery);
                insertPst.setString(1, item.pid);
                insertPst.setString(2, item.productName);
                insertPst.setDouble(3, item.price);
                insertPst.setString(4, buyerCid);
                insertPst.setString(5, item.sellerCid);
                insertPst.setInt(6, item.quantity);
                
                insertPst.executeUpdate();
            }
            
            // Commit the transaction
            transactionCon.commit();
            
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (transactionCon != null) {
                    transactionCon.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error processing purchase: " + e.getMessage(), 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
            success = false;
        } finally {
            try {
                if (transactionCon != null) {
                    transactionCon.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
    
    // Main method for testing
    public static void main(String[] args) {
        // This would be implemented for testing purposes
    }
}
