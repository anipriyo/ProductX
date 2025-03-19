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
//import java.sql.*;
//
//public class ProfilePage {
//    private JFrame frame;
//    private String cid;
//    private Connection con;
//    private JTextField txtName, txtEmail, txtPhone, txtAddress;
//    private JPasswordField txtNewPassword;
//    private JLabel lblStatus, lblName, lblCid;
//
//    public ProfilePage(String cid, Connection con) {
//        this.cid = cid;
//        this.con = con;
//        initializeUI();
//    }
//
//    private void initializeUI() {
//        frame = new JFrame("Profile Page");
//        frame.setSize(400, 500);
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setLayout(new GridLayout(9, 2, 10, 10));
//
//        // Fetch user details
//        String[] userDetails = getUserDetails();
//
//        // Display Name & CID
//        lblName = new JLabel("Name: " + userDetails[0], SwingConstants.CENTER);
//        lblCid = new JLabel("CID: " + cid, SwingConstants.CENTER);
//        frame.add(lblName);
//        frame.add(lblCid);
//
//        // Labels and Fields
//        frame.add(new JLabel("Name:"));
//        txtName = new JTextField(userDetails[0]);
//        frame.add(txtName);
//
//        frame.add(new JLabel("Email:"));
//        txtEmail = new JTextField(userDetails[1]);
//        frame.add(txtEmail);
//
//        frame.add(new JLabel("Phone:"));
//        txtPhone = new JTextField(userDetails[2]);
//        frame.add(txtPhone);
//
//        frame.add(new JLabel("Address:"));
//        txtAddress = new JTextField(userDetails[3]);
//        frame.add(txtAddress);
//
//        frame.add(new JLabel("New Password:"));
//        txtNewPassword = new JPasswordField();
//        frame.add(txtNewPassword);
//
//        // Buttons
//        JButton btnUpdate = new JButton("Update Details");
//        btnUpdate.addActionListener(this::updateDetails);
//        frame.add(btnUpdate);
//
//        JButton btnBack = new JButton("Back");
//        btnBack.addActionListener(e -> backToMain());
//        frame.add(btnBack);
//
//        lblStatus = new JLabel("", SwingConstants.CENTER);
//        frame.add(lblStatus);
//
//        frame.setVisible(true);
//    }
//
//    private String[] getUserDetails() {
//        try {
//            System.out.println("Fetching details for CID: " + cid);
//
//            if (con == null) {
//                System.out.println("Database connection is NULL!");
//                return new String[]{"(DB Error)", "(DB Error)", "(DB Error)", "(DB Error)"};
//            }
//
//            String query = "SELECT Name, Email, Phone_Number, Address FROM customer WHERE CID = ?";
//            PreparedStatement pst = con.prepareStatement(query);
//            pst.setString(1, cid);
//            ResultSet rs = pst.executeQuery();
//
//            if (rs.next()) {
//                String name = rs.getString("Name");
//                String email = rs.getString("Email");
//                String phone = rs.getString("Phone_Number");
//                String address = rs.getString("Address");
//
//                // Handle NULL values
//                name = (name != null) ? name : "Not Provided";
//                email = (email != null) ? email : "Not Provided";
//                phone = (phone != null) ? phone : "Not Provided";
//                address = (address != null) ? address : "Not Provided";
//
//                System.out.println("Data Fetched: " + name + ", " + email + ", " + phone + ", " + address);
//                return new String[]{name, email, phone, address};
//            } else {
//                System.out.println("No data found for CID: " + cid);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return new String[]{"(Not Found)", "(Not Found)", "(Not Found)", "(Not Found)"};
//    }
//
//    private void updateDetails(ActionEvent e) {
//    try {
//        String newName = txtName.getText().trim();
//        String newEmail = txtEmail.getText().trim();
//        String newPhone = txtPhone.getText().trim();
//        String newPassword = new String(txtNewPassword.getPassword()).trim();
//
//        // Fetch current values to compare
//        String[] currentDetails = getUserDetails();
//
//        boolean updated = false;
//
//        if (!newName.isEmpty() && !newName.equals(currentDetails[0])) {
//            updateField("name", newName);
//            updated = true;
//        }
//
//        if (!newEmail.isEmpty() && !newEmail.equals(currentDetails[1])) {
//            updateField("email", newEmail);
//            updated = true;
//        }
//
//        if (!newPhone.isEmpty() && !newPhone.equals(currentDetails[2])) {
//            updateField("phone", newPhone);
//            updated = true;
//        }
//
//        if (!newPassword.isEmpty()) {
//            String encryptedPassword = ProductX.hashPassword(newPassword);
//            updateField("password", encryptedPassword);
//            updated = true;
//        }
//
//        if (updated) {
//            JOptionPane.showMessageDialog(frame, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
//            lblStatus.setText("Profile updated successfully!");
//        } else {
//            lblStatus.setText("No changes made.");
//        }
//    } catch (SQLException ex) {
//        ex.printStackTrace();
//        lblStatus.setText("Error updating profile.");
//    }
//}
//
//
//    private void updateField(String field, String value) throws SQLException {
//        String query = "UPDATE customer SET " + field + " = ? WHERE CID = ?";
//        PreparedStatement pst = con.prepareStatement(query);
//        pst.setString(1, value);
//        pst.setString(2, cid);
//        pst.executeUpdate();
//    }
//
//    private void backToMain() {
//        frame.dispose();
////        new MainPage(cid, con);
//    }
//}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class ProfilePage {
    private JFrame frame;
    private String cid;
    private Connection con;
    private JTextField txtName, txtEmail, txtPhone, txtAddress;
    private JPasswordField txtNewPassword, txtConfirmPassword;
    private JLabel lblStatus, lblName, lblCid;
    private JPanel mainPanel, buttonPanel;
    private Color primaryColor = new Color(51, 102, 255);
    private Color secondaryColor = new Color(240, 240, 240);

    public ProfilePage(String cid, Connection con) {
        this.cid = cid;
        this.con = con;
        initializeUI();
    }

    private void initializeUI() {
        // Frame setup
        frame = new JFrame("Profile Management");
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel with border layout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Fetch user details
        String[] userDetails = getUserDetails();
        
        // Title
        JLabel titleLabel = new JLabel("Profile Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // User info
        JPanel userInfoPanel = new JPanel(new GridLayout(2, 1));
        userInfoPanel.setOpaque(false);
        
        lblName = new JLabel("User: " + userDetails[0], SwingConstants.LEFT);
        lblName.setFont(new Font("Arial", Font.BOLD, 14));
        lblName.setForeground(Color.WHITE);
        
        lblCid = new JLabel("CID: " + cid, SwingConstants.LEFT);
        lblCid.setFont(new Font("Arial", Font.PLAIN, 14));
        lblCid.setForeground(Color.WHITE);
        
        userInfoPanel.add(lblName);
        userInfoPanel.add(lblCid);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        // Fetch user details
        String[] userDetails = getUserDetails();
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(secondaryColor, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblNameField = new JLabel("Name:");
        lblNameField.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblNameField, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        txtName = new JTextField(userDetails[0]);
        txtName.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtName, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel lblEmailField = new JLabel("Email:");
        lblEmailField.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblEmailField, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(userDetails[1]);
        txtEmail.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtEmail, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        JLabel lblPhoneField = new JLabel("Phone:");
        lblPhoneField.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblPhoneField, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        txtPhone = new JTextField(userDetails[2]);
        txtPhone.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtPhone, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        JLabel lblAddressField = new JLabel("Address:");
        lblAddressField.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblAddressField, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        txtAddress = new JTextField(userDetails[3]);
        txtAddress.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtAddress, gbc);
        
        // New Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        JLabel lblNewPasswordField = new JLabel("New Password:");
        lblNewPasswordField.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblNewPasswordField, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        txtNewPassword = new JPasswordField();
        txtNewPassword.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtNewPassword, gbc);
        
        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        JLabel lblConfirmPasswordField = new JLabel("Confirm Password:");
        lblConfirmPasswordField.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(lblConfirmPasswordField, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setPreferredSize(new Dimension(200, 30));
        formPanel.add(txtConfirmPassword, gbc);
        
        // Status Label
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 12));
        lblStatus.setForeground(Color.RED);
        formPanel.add(lblStatus, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnUpdate = new JButton("Update Profile");
        btnUpdate.setBackground(primaryColor);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.addActionListener(this::updateDetails);
        
        JButton btnBack = new JButton("Back to Main");
        btnBack.setBackground(secondaryColor);
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> backToMain());
        
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnBack);
        
        return buttonPanel;
    }

    private String[] getUserDetails() {
        try {
            System.out.println("Fetching details for CID: " + cid);

            if (con == null) {
                System.out.println("Database connection is NULL!");
                return new String[]{"(DB Error)", "(DB Error)", "(DB Error)", "(DB Error)"};
            }

            String query = "SELECT Name, Email, Phone_Number, Address FROM customer WHERE CID = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, cid);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone_Number");
                String address = rs.getString("Address");

                // Handle NULL values
                name = (name != null) ? name : "Not Provided";
                email = (email != null) ? email : "Not Provided";
                phone = (phone != null) ? phone : "Not Provided";
                address = (address != null) ? address : "Not Provided";

                System.out.println("Data Fetched: " + name + ", " + email + ", " + phone + ", " + address);
                return new String[]{name, email, phone, address};
            } else {
                System.out.println("No data found for CID: " + cid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[]{"(Not Found)", "(Not Found)", "(Not Found)", "(Not Found)"};
    }
    
    private boolean validateInput() {
        // Check phone number format
        String phonePattern = "\\d{10}";
        if (!txtPhone.getText().trim().matches(phonePattern)) {
            lblStatus.setText("Phone number must be 10 digits");
            return false;
        }
        
        // Check email format
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!txtEmail.getText().trim().isEmpty() && !txtEmail.getText().trim().matches(emailPattern)) {
            lblStatus.setText("Invalid email format");
            return false;
        }
        
        // Check password confirmation
        String newPassword = new String(txtNewPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            lblStatus.setText("Passwords do not match");
            return false;
        }
        
        return true;
    }

    private void updateDetails(ActionEvent e) {
        try {
            if (!validateInput()) {
                return;
            }
            
            String newName = txtName.getText().trim();
            String newEmail = txtEmail.getText().trim();
            String newPhone = txtPhone.getText().trim();
            String newAddress = txtAddress.getText().trim();
            String newPassword = new String(txtNewPassword.getPassword()).trim();

            // Fetch current values to compare
            String[] currentDetails = getUserDetails();

            boolean updated = false;
            
            // Show progress indicator
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            lblStatus.setText("Updating profile...");
            lblStatus.setForeground(new Color(0, 102, 204));

            if (!newName.isEmpty() && !newName.equals(currentDetails[0])) {
                updateField("Name", newName);
                lblName.setText("User: " + newName);
                updated = true;
            }

            if (!newEmail.isEmpty() && !newEmail.equals(currentDetails[1])) {
                updateField("Email", newEmail);
                updated = true;
            }

            if (!newPhone.isEmpty() && !newPhone.equals(currentDetails[2])) {
                updateField("Phone_Number", newPhone);
                updated = true;
            }
            
            if (!newAddress.isEmpty() && !newAddress.equals(currentDetails[3])) {
                updateField("Address", newAddress);
                updated = true;
            }

            if (!newPassword.isEmpty()) {
                String encryptedPassword = ProductX.hashPassword(newPassword);
                updateField("Password", encryptedPassword);
                // Clear password fields after update
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
                updated = true;
            }

            // Reset cursor
            setCursor(Cursor.getDefaultCursor());
            
            if (updated) {
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                lblStatus.setText("Profile updated successfully!");
                lblStatus.setForeground(new Color(0, 153, 0));
            } else {
                lblStatus.setText("No changes made.");
                lblStatus.setForeground(Color.BLUE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            lblStatus.setText("Error updating profile: " + ex.getMessage());
            lblStatus.setForeground(Color.RED);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void updateField(String field, String value) throws SQLException {
        String query = "UPDATE customer SET " + field + " = ? WHERE CID = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, value);
        pst.setString(2, cid);
        pst.executeUpdate();
    }
    
    private void setCursor(Cursor cursor) {
        frame.setCursor(cursor);
        mainPanel.setCursor(cursor);
        buttonPanel.setCursor(cursor);
    }

    private void backToMain() {
        frame.dispose();
        // Uncomment the following line when MainPage is available
        // new MainPage(cid, con);
    }
}