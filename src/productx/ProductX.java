/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package productx;

/**
 *
 * @author annae
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ProductX {
    private static Connection con;

    public static void main(String[] args) {
        // Establish database connection
        connectToDatabase();

        // Show the Login/Signup GUI
        SwingUtilities.invokeLater(ProductX::showLoginSignup);
    }

    private static void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/productx", "root", "ProductX@password");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            int retry = JOptionPane.showConfirmDialog(null, "Retry?", "Database Error", JOptionPane.YES_NO_OPTION);
            if (retry == JOptionPane.YES_OPTION) {
                connectToDatabase();
            } else {
                System.exit(0);
            }
        }
    }

    private static void showLoginSignup() {
        JFrame frame = new JFrame("Login / Signup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 1));

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Signup");

        loginBtn.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });

        signupBtn.addActionListener(e -> {
            frame.dispose();
            showSignupScreen();
        });

        frame.add(new JLabel("Welcome to ProductX", SwingConstants.CENTER));
        frame.add(loginBtn);
        frame.add(signupBtn);
        frame.setVisible(true);
    }

    private static void showLoginScreen() {
        JFrame frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new GridLayout(4, 2));

        JLabel lblCID = new JLabel("User ID (CID):");
        JTextField txtCID = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");

        loginBtn.addActionListener(e -> {
            String cid = txtCID.getText();
            String password = new String(txtPassword.getPassword());

            if (authenticateUser(cid, password)) {
                frame.dispose();
                showSuccessScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid User ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            frame.dispose();
            showLoginSignup();
        });

        frame.add(lblCID);
        frame.add(txtCID);
        frame.add(lblPassword);
        frame.add(txtPassword);
        frame.add(loginBtn);
        frame.add(backBtn);
        frame.setVisible(true);
    }

    private static void showSignupScreen() {
        JFrame frame = new JFrame("Signup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(6, 2));

        JTextField txtName = new JTextField();
        JTextField txtAddress = new JTextField();
        JTextField txtPhone = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JButton signupBtn = new JButton("Signup");
        JButton backBtn = new JButton("Back");

        signupBtn.addActionListener(e -> {
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String password = new String(txtPassword.getPassword());

            if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cid = registerUser(name, address, phone, password);
            if (cid != null) {
                JOptionPane.showMessageDialog(frame, "Signup Successful! Your User ID (CID): " + cid);
                frame.dispose();
                showLoginScreen();
            } else {
                JOptionPane.showMessageDialog(frame, "Signup Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            frame.dispose();
            showLoginSignup();
        });

        frame.add(new JLabel("Name:"));
        frame.add(txtName);
        frame.add(new JLabel("Address:"));
        frame.add(txtAddress);
        frame.add(new JLabel("Phone Number:"));
        frame.add(txtPhone);
        frame.add(new JLabel("Password:"));
        frame.add(txtPassword);
        frame.add(signupBtn);
        frame.add(backBtn);
        frame.setVisible(true);
    }

    private static void showSuccessScreen() {
        JFrame frame = new JFrame("Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        JLabel successLabel = new JLabel("Successfully Logged In!", SwingConstants.CENTER);
        JButton logoutBtn = new JButton("Logout");

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            showLoginSignup();
        });

        frame.add(successLabel, BorderLayout.CENTER);
        frame.add(logoutBtn, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

private static boolean authenticateUser(String cid, String password) {
    try {
        PreparedStatement ps = con.prepareStatement("SELECT Password FROM Customer WHERE CID = ?");
        ps.setString(1, cid);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String storedHash = rs.getString("Password");
            String enteredHash = hashPassword(password);
            System.out.println("Stored Hash: " + storedHash);
            System.out.println("Entered Hash: " + enteredHash);
            return storedHash.equals(enteredHash);
        } else {
            System.out.println("CID not found: " + cid);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


private static String registerUser(String name, String address, String phone, String password) {
    try {
        String cid;
        do {
            cid = generateCID();
            System.out.println("Generated CID: " + cid);
        } while (cidExists(cid));

        System.out.println("Inserting User: " + name + ", " + cid);

        PreparedStatement ps = con.prepareStatement("INSERT INTO Customer (CID, Name, Address, Phone_Number, Password) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, cid);
        ps.setString(2, name);
        ps.setString(3, address);
        ps.setString(4, phone);
        ps.setString(5, hashPassword(password));
        ps.executeUpdate();

        System.out.println("User registered successfully with CID: " + cid);
        return cid;
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}


    private static boolean cidExists(String cid) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT CID FROM Customer WHERE CID = ?");
        ps.setString(1, cid);
        return ps.executeQuery().next();
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateCID() {
        return String.valueOf(10000000 + new Random().nextInt(90000000));
    }
}

