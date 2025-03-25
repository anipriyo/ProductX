/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package productx;

/**
 *
 * @author annae
 */
//import javax.swing.*;
//import java.awt.*;
//import java.sql.*;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Random;
//
//public class ProductX {
//    private static Connection con;
//
//    public static void main(String[] args) {
//        // Establish database connection
//        connectToDatabase();
//
//        // Show the Login/Signup GUI
//        SwingUtilities.invokeLater(ProductX::showLoginSignup);
//    }
//
//    private static void connectToDatabase() {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/productx", "root", "ProductX@password");
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Database Connection Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//            int retry = JOptionPane.showConfirmDialog(null, "Retry?", "Database Error", JOptionPane.YES_NO_OPTION);
//            if (retry == JOptionPane.YES_OPTION) {
//                connectToDatabase();
//            } else {
//                System.exit(0);
//            }
//        }
//    }
//
//    private static void showLoginSignup() {
//        JFrame frame = new JFrame("Login / Signup");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 300);
//        frame.setLayout(new GridLayout(3, 1));
//
//        JButton loginBtn = new JButton("Login");
//        JButton signupBtn = new JButton("Signup");
//
//        loginBtn.addActionListener(e -> {
//            frame.dispose();
//            showLoginScreen();
//        });
//
//        signupBtn.addActionListener(e -> {
//            frame.dispose();
//            showSignupScreen();
//        });
//
//        frame.add(new JLabel("Welcome to ProductX", SwingConstants.CENTER));
//        frame.add(loginBtn);
//        frame.add(signupBtn);
//        frame.setVisible(true);
//    }
//
//    private static void showLoginScreen() {
//        JFrame frame = new JFrame("Login");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 250);
//        frame.setLayout(new GridLayout(4, 2));
//
//        JLabel lblCID = new JLabel("User ID (CID):");
//        JTextField txtCID = new JTextField();
//        JLabel lblPassword = new JLabel("Password:");
//        JPasswordField txtPassword = new JPasswordField();
//        JButton loginBtn = new JButton("Login");
//        JButton backBtn = new JButton("Back");
//
//        loginBtn.addActionListener(e -> {
//            String cid = txtCID.getText();
//            String password = new String(txtPassword.getPassword());
//
//            if (authenticateUser(cid, password)) {
//                frame.dispose();
//                showSuccessScreen(cid);
//            } else {
//                JOptionPane.showMessageDialog(frame, "Invalid User ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//
//        backBtn.addActionListener(e -> {
//            frame.dispose();
//            showLoginSignup();
//        });
//
//        frame.add(lblCID);
//        frame.add(txtCID);
//        frame.add(lblPassword);
//        frame.add(txtPassword);
//        frame.add(loginBtn);
//        frame.add(backBtn);
//        frame.setVisible(true);
//    }
//
//    private static void showSignupScreen() {
//        JFrame frame = new JFrame("Signup");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 400);
//        frame.setLayout(new GridLayout(6, 2));
//
//        JTextField txtName = new JTextField();
//        JTextField txtAddress = new JTextField();
//        JTextField txtPhone = new JTextField();
//        JPasswordField txtPassword = new JPasswordField();
//        JButton signupBtn = new JButton("Signup");
//        JButton backBtn = new JButton("Back");
//
//        signupBtn.addActionListener(e -> {
//            String name = txtName.getText();
//            String address = txtAddress.getText();
//            String phone = txtPhone.getText();
//            String password = new String(txtPassword.getPassword());
//
//            if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
//                JOptionPane.showMessageDialog(frame, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            String cid = registerUser(name, address, phone, password);
//            if (cid != null) {
//                JOptionPane.showMessageDialog(frame, "Signup Successful! Your User ID (CID): " + cid);
//                frame.dispose();
//                showLoginScreen();
//            } else {
//                JOptionPane.showMessageDialog(frame, "Signup Failed", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//
//        backBtn.addActionListener(e -> {
//            frame.dispose();
//            showLoginSignup();
//        });
//
//        frame.add(new JLabel("Name:"));
//        frame.add(txtName);
//        frame.add(new JLabel("Address:"));
//        frame.add(txtAddress);
//        frame.add(new JLabel("Phone Number:"));
//        frame.add(txtPhone);
//        frame.add(new JLabel("Password:"));
//        frame.add(txtPassword);
//        frame.add(signupBtn);
//        frame.add(backBtn);
//        frame.setVisible(true);
//    }
//
//    private static void showSuccessScreen(String cid) {
//    new MainPage(cid, con);  // Pass CID and connection
//}
//
//
//private static boolean authenticateUser(String cid, String password) {
//    try {
//        PreparedStatement ps = con.prepareStatement("SELECT Password FROM Customer WHERE CID = ?");
//        ps.setString(1, cid);
//        ResultSet rs = ps.executeQuery();
//
//        if (rs.next()) {
//            String storedHash = rs.getString("Password");
//            String enteredHash = hashPassword(password);
//            System.out.println("Stored Hash: " + storedHash);
//            System.out.println("Entered Hash: " + enteredHash);
//            return storedHash.equals(enteredHash);
//        } else {
//            System.out.println("CID not found: " + cid);
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    return false;
//}
//
//
//private static String registerUser(String name, String address, String phone, String password) {
//    try {
//        String cid;
//        do {
//            cid = generateCID();
//            System.out.println("Generated CID: " + cid);
//        } while (cidExists(cid));
//
//        System.out.println("Inserting User: " + name + ", " + cid);
//
//        PreparedStatement ps = con.prepareStatement("INSERT INTO Customer (CID, Name, Address, Phone_Number, Password) VALUES (?, ?, ?, ?, ?)");
//        ps.setString(1, cid);
//        ps.setString(2, name);
//        ps.setString(3, address);
//        ps.setString(4, phone);
//        ps.setString(5, hashPassword(password));
//        ps.executeUpdate();
//
//        System.out.println("User registered successfully with CID: " + cid);
//        return cid;
//    } catch (SQLException e) {
//        e.printStackTrace();
//        return null;
//    }
//}
//
//
//    private static boolean cidExists(String cid) throws SQLException {
//        PreparedStatement ps = con.prepareStatement("SELECT CID FROM Customer WHERE CID = ?");
//        ps.setString(1, cid);
//        return ps.executeQuery().next();
//    }
//
//    protected static String hashPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedBytes = md.digest(password.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hashedBytes) sb.append(String.format("%02x", b));
//            return sb.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static String generateCID() {
//        return String.valueOf(10000000 + new Random().nextInt(90000000));
//    }
//}
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Pattern;
import javax.swing.border.EmptyBorder;

public class ProductX {

    private static Connection con;
    private static final Color PRIMARY_COLOR2 = new Color(41, 128, 185); // Blue
    private static final Color ACCENT_COLOR = new Color(39, 174, 96);   // Green
    private static final Color BG_COLOR = new Color(245, 245, 245);     // Light Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);      // Dark Blue
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    private static void showAdminLoginScreen() {
        JFrame frame = createBaseFrame("Admin Login", 400, 250);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(BG_COLOR);
        // Title
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR2);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Password Field
        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        passPanel.setBackground(BG_COLOR);
        JLabel passLabel = new JLabel("Admin Password:");
        passLabel.setFont(LABEL_FONT);
        passLabel.setForeground(TEXT_COLOR);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(LABEL_FONT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtPassword);
        passPanel.add(passLabel, BorderLayout.NORTH);
        passPanel.add(txtPassword, BorderLayout.CENTER);
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 50));
        JButton loginBtn = createStyledButton("Login", PRIMARY_COLOR2);
        JButton backBtn = createStyledButton("Back", Color.GRAY);
        loginBtn.addActionListener(e -> {
            String password = new String(txtPassword.getPassword());
            String hashedPassword = hashPassword(password);
            String predefinedAdminHash = hashPassword("Password@ProductX");
            try {
                // Validate connection explicitly
                if (con == null || con.isClosed()) {
                    JOptionPane.showMessageDialog(frame,
                            "Database connection is not established. Reconnecting...",
                            "Connection Error",
                            JOptionPane.ERROR_MESSAGE);
                    connectToDatabase();
                    return;
                }
                // Check if the entered password matches the predefined admin hash
                if (hashedPassword.equals(predefinedAdminHash)) {
                    frame.dispose();
                    // Pass connection explicitly
                    SwingUtilities.invokeLater(() -> new AdminPage(con));
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Incorrect Admin Password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame,
                        "Database error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        backBtn.addActionListener(e -> {
            frame.dispose();
            showLoginSignup();
        });
        buttonPanel.add(loginBtn);
        buttonPanel.add(backBtn);
        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(passPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        JFrame frame = createBaseFrame("Welcome to ProductX", 500, 400);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(BG_COLOR);

        // Logo/Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BG_COLOR);
        JLabel logoLabel = new JLabel("ProductX");
        logoLabel.setFont(TITLE_FONT);
        logoLabel.setForeground(PRIMARY_COLOR2);
        titlePanel.add(logoLabel);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome! Please log in or create an account");
        welcomeLabel.setFont(LABEL_FONT);
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        JButton loginBtn = createStyledButton("Login", PRIMARY_COLOR2);
        JButton signupBtn = createStyledButton("Create Account", ACCENT_COLOR);
        JButton adminLoginBtn = createStyledButton("Admin Login", Color.DARK_GRAY);
        adminLoginBtn.addActionListener(e -> {
            frame.dispose();
            showAdminLoginScreen();
        });

        loginBtn.addActionListener(e -> {
            frame.dispose();
            showLoginScreen();
        });

        signupBtn.addActionListener(e -> {
            frame.dispose();
            showSignupScreen();
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(adminLoginBtn);
        buttonPanel.add(signupBtn);

        // Add components to main panel with spacing
        mainPanel.add(titlePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void showLoginScreen() {
        JFrame frame = createBaseFrame("Login to ProductX", 500, 400);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(BG_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR2);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        formPanel.setBackground(BG_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        // CID Field
        JPanel cidPanel = new JPanel(new BorderLayout(10, 0));
        cidPanel.setBackground(BG_COLOR);
        JLabel cidLabel = new JLabel("User ID (CID):");
        cidLabel.setFont(LABEL_FONT);
        cidLabel.setForeground(TEXT_COLOR);
        JTextField txtCID = new JTextField();
        txtCID.setFont(LABEL_FONT);
        txtCID.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtCID);
        cidPanel.add(cidLabel, BorderLayout.NORTH);
        cidPanel.add(txtCID, BorderLayout.CENTER);

        // Password Field
        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        passPanel.setBackground(BG_COLOR);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(LABEL_FONT);
        passLabel.setForeground(TEXT_COLOR);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(LABEL_FONT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtPassword);
        passPanel.add(passLabel, BorderLayout.NORTH);
        passPanel.add(txtPassword, BorderLayout.CENTER);

        // Remember me checkbox
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsPanel.setBackground(BG_COLOR);
        JCheckBox rememberMe = new JCheckBox("Remember me");
        rememberMe.setFont(new Font("Arial", Font.PLAIN, 12));
        rememberMe.setBackground(BG_COLOR);
        optionsPanel.add(rememberMe);

        formPanel.add(cidPanel);
        formPanel.add(passPanel);
        formPanel.add(optionsPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        JButton loginBtn = createStyledButton("Login", PRIMARY_COLOR2);
        JButton backBtn = createStyledButton("Back", Color.GRAY);

        loginBtn.addActionListener(e -> {
            String cid = txtCID.getText();
            String password = new String(txtPassword.getPassword());

            if (cid.isEmpty() || password.isEmpty()) {
                showError(frame, "Please fill in all fields");
                return;
            }

            if (authenticateUser(cid, password)) {
                frame.dispose();
                showSuccessScreen(cid);
            } else {
                showError(frame, "Invalid User ID or Password");
            }
        });

        backBtn.addActionListener(e -> {
            frame.dispose();
            showLoginSignup();
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(backBtn);

        // Forgot password link
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(BG_COLOR);
        JLabel forgotPassword = new JLabel("Forgot password?");
        forgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPassword.setForeground(PRIMARY_COLOR2);
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkPanel.add(forgotPassword);

        forgotPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(frame,
                        "Please contact support to reset your password.",
                        "Password Reset",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(linkPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void showSignupScreen() {
        JFrame frame = createBaseFrame("Create Account - ProductX", 500, 550);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(BG_COLOR);

        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR2);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 1, 0, 10));
        formPanel.setBackground(BG_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Name Field
        JPanel namePanel = new JPanel(new BorderLayout(10, 0));
        namePanel.setBackground(BG_COLOR);
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(LABEL_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        JTextField txtName = new JTextField();
        txtName.setFont(LABEL_FONT);
        txtName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtName);
        namePanel.add(nameLabel, BorderLayout.NORTH);
        namePanel.add(txtName, BorderLayout.CENTER);

        // Address Field
        JPanel addressPanel = new JPanel(new BorderLayout(10, 0));
        addressPanel.setBackground(BG_COLOR);
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(LABEL_FONT);
        addressLabel.setForeground(TEXT_COLOR);
        JTextField txtAddress = new JTextField();
        txtAddress.setFont(LABEL_FONT);
        txtAddress.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtAddress);
        addressPanel.add(addressLabel, BorderLayout.NORTH);
        addressPanel.add(txtAddress, BorderLayout.CENTER);

        // Phone Field
        JPanel phonePanel = new JPanel(new BorderLayout(10, 0));
        phonePanel.setBackground(BG_COLOR);
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(LABEL_FONT);
        phoneLabel.setForeground(TEXT_COLOR);
        JTextField txtPhone = new JTextField();
        txtPhone.setFont(LABEL_FONT);
        txtPhone.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtPhone);
        phonePanel.add(phoneLabel, BorderLayout.NORTH);
        phonePanel.add(txtPhone, BorderLayout.CENTER);

        // Password Field
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
        passwordPanel.setBackground(BG_COLOR);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(LABEL_FONT);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtPassword);
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);

        // Confirm Password Field
        JPanel confirmPanel = new JPanel(new BorderLayout(10, 0));
        confirmPanel.setBackground(BG_COLOR);
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(LABEL_FONT);
        confirmLabel.setForeground(TEXT_COLOR);
        JPasswordField txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(LABEL_FONT);
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        addFocusEffect(txtConfirmPassword);
        confirmPanel.add(confirmLabel, BorderLayout.NORTH);
        confirmPanel.add(txtConfirmPassword, BorderLayout.CENTER);

        formPanel.add(namePanel);
        formPanel.add(addressPanel);
        formPanel.add(phonePanel);
        formPanel.add(passwordPanel);
        formPanel.add(confirmPanel);

        // Password strength indicator
        JPanel strengthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        strengthPanel.setBackground(BG_COLOR);
        JProgressBar passwordStrength = new JProgressBar(0, 100);
        passwordStrength.setPreferredSize(new Dimension(150, 10));
        JLabel strengthLabel = new JLabel("Password Strength: Weak");
        strengthLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        strengthPanel.add(strengthLabel);
        strengthPanel.add(passwordStrength);

        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String pass = new String(txtPassword.getPassword());
                int strength = calculatePasswordStrength(pass);
                passwordStrength.setValue(strength);

                if (strength < 30) {
                    strengthLabel.setText("Password Strength: Weak");
                    passwordStrength.setForeground(Color.RED);
                } else if (strength < 70) {
                    strengthLabel.setText("Password Strength: Medium");
                    passwordStrength.setForeground(Color.ORANGE);
                } else {
                    strengthLabel.setText("Password Strength: Strong");
                    passwordStrength.setForeground(Color.GREEN);
                }
            }
        });

        // Terms and conditions
        JPanel termsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        termsPanel.setBackground(BG_COLOR);
        JCheckBox agreeTerms = new JCheckBox("I agree to the Terms and Conditions");
        agreeTerms.setBackground(BG_COLOR);
        agreeTerms.setFont(new Font("Arial", Font.PLAIN, 12));
        termsPanel.add(agreeTerms);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(new EmptyBorder(0, 50, 0, 50));

        JButton signupBtn = createStyledButton("Create Account", ACCENT_COLOR);
        JButton backBtn = createStyledButton("Back", Color.GRAY);

        signupBtn.addActionListener(e -> {
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            // Validate inputs
            if (name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                showError(frame, "Fields marked with * cannot be empty!");
                return;
            }

            if (!validatePhone(phone)) {
                showError(frame, "Please enter a valid phone number");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showError(frame, "Passwords do not match!");
                return;
            }

            if (calculatePasswordStrength(password) < 30) {
                showError(frame, "Password is too weak. Please use a stronger password.");
                return;
            }

            if (!agreeTerms.isSelected()) {
                showError(frame, "You must agree to the Terms and Conditions");
                return;
            }

            String cid = registerUser(name, address, phone, password);
            if (cid != null) {
                JOptionPane.showMessageDialog(frame,
                        "Account created successfully!\nYour User ID (CID): " + cid,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                showLoginScreen();
            } else {
                showError(frame, "Registration Failed. Please try again later.");
            }
        });

        backBtn.addActionListener(e -> {
            frame.dispose();
            showLoginSignup();
        });

        buttonPanel.add(signupBtn);
        buttonPanel.add(backBtn);

        // Add components to main panel
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(formPanel);
        mainPanel.add(strengthPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(termsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void addFocusEffect(JComponent component) {
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(PRIMARY_COLOR2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                component.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
            }
        });
    }

    private static JFrame createBaseFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setResizable(false);
        return frame;
    }

    private static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private static void showSuccessScreen(String cid) {
        new MainPage(cid, con);  // Pass CID and connection
    }

    private static boolean authenticateUser(String cid, String password) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT Password FROM Customer WHERE CID = ?");
            ps.setString(1, cid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("Password");
                String enteredHash = hashPassword(password);
                return storedHash.equals(enteredHash);
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
            } while (cidExists(cid));

            PreparedStatement ps = con.prepareStatement("INSERT INTO Customer (CID, Name, Address, Phone_Number, Password) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, cid);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, phone);
            ps.setString(5, hashPassword(password));
            ps.executeUpdate();

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

    protected static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateCID() {
        return String.valueOf(10000000 + new Random().nextInt(90000000));
    }

    private static int calculatePasswordStrength(String password) {
        int strength = 0;

        // Add points for length
        if (password.length() >= 8) {
            strength += 20;
        }
        if (password.length() >= 12) {
            strength += 10;
        }

        // Add points for complexity
        if (Pattern.compile("[A-Z]").matcher(password).find()) {
            strength += 15;
        }
        if (Pattern.compile("[a-z]").matcher(password).find()) {
            strength += 15;
        }
        if (Pattern.compile("[0-9]").matcher(password).find()) {
            strength += 15;
        }
        if (Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) {
            strength += 25;
        }

        return Math.min(100, strength);
    }

    private static boolean validatePhone(String phone) {
        // Simple validation - can be made more sophisticated
        return phone.matches("\\d{10,15}");
    }

    private static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}