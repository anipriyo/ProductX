/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productx;
//package productx;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

public class ContactForm extends JFrame {
    // Color and Font Constants
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Blue
    private static final Color ACCENT_COLOR = new Color(39, 174, 96);   // Green
    private static final Color BG_COLOR = new Color(245, 245, 245);     // Light Gray
    private static final Color TEXT_COLOR = new Color(44, 62, 80);      // Dark Blue
    private static final Color ERROR_COLOR = new Color(231, 76, 60);    // Red
    
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    // Form Components
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea messageArea;
    private JButton submitButton;
    private JLabel statusLabel;

    public ContactForm() {
        initializeUI();
    }

    private void initializeUI() {
        // Frame Setup
        setTitle("Contact Us");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel
        JPanel mainPanel = createMainPanel();
        add(mainPanel);

        // Add input validation
        addInputValidation();
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Panel
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);

        // Form Panel
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BG_COLOR);
        JLabel titleLabel = new JLabel("Contact Us");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabeledField("Full Name:", nameField = new JTextField(20), gbc));

        // Email Field
        gbc.gridy = 1;
        formPanel.add(createLabeledField("Email:", emailField = new JTextField(20), gbc));

        // Phone Field
        gbc.gridy = 2;
        formPanel.add(createLabeledField("Phone (Optional):", phoneField = new JTextField(20), gbc));

        // Message Area
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setFont(LABEL_FONT);
        formPanel.add(messageLabel, gbc);

        gbc.gridy = 4;
        messageArea = new JTextArea(5, 20);
        messageArea.setFont(LABEL_FONT);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        formPanel.add(scrollPane, gbc);

        // Submit Button
        gbc.gridy = 5;
        submitButton = createSubmitButton();
        formPanel.add(submitButton, gbc);

        // Status Label
        gbc.gridy = 6;
        statusLabel = new JLabel("");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        formPanel.add(statusLabel, gbc);

        return formPanel;
    }

    private JPanel createLabeledField(String labelText, JTextField textField, GridBagConstraints gbc) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);

        GridBagConstraints labelGbc = (GridBagConstraints) gbc.clone();
        labelGbc.gridx = 0;
        labelGbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        panel.add(label, labelGbc);

        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = 1;
        textField.setFont(LABEL_FONT);
        panel.add(textField, fieldGbc);

        return panel;
    }

    private JButton createSubmitButton() {
        JButton button = new JButton("Send Message");
        button.setFont(BUTTON_FONT);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(e -> submitForm());
        return button;
    }

    private void addInputValidation() {
        // Real-time validation
        nameField.addFocusListener(new ValidationFocusListener(nameField, this::validateName));
        emailField.addFocusListener(new ValidationFocusListener(emailField, this::isValidEmail));
        phoneField.addFocusListener(new ValidationFocusListener(phoneField, this::isValidPhoneOrEmpty));
    }

    private void submitForm() {
        // Reset status
        statusLabel.setText("");
        statusLabel.setForeground(Color.BLACK);

        // Validate all fields
        if (!validateName(nameField.getText().trim())) {
            showError("Please enter a valid name (minimum 2 characters)");
            return;
        }

        if (!isValidEmail(emailField.getText().trim())) {
            showError("Please enter a valid email address");
            return;
        }

        String phone = phoneField.getText().trim();
        if (!isValidPhoneOrEmpty(phone)) {
            showError("Please enter a valid phone number or leave blank");
            return;
        }

        String message = messageArea.getText().trim();
        if (message.isEmpty() || message.length() < 10) {
            showError("Please enter a message (minimum 10 characters)");
            return;
        }

        // If all validations pass
        try {
            // Simulate form submission (replace with actual submission logic)
            submitMessage(nameField.getText().trim(), 
                          emailField.getText().trim(), 
                          phone, 
                          message);
            
            // Clear form
            clearForm();
            
            // Show success
            statusLabel.setForeground(ACCENT_COLOR);
            statusLabel.setText("Message sent successfully!");
        } catch (Exception e) {
            showError("Failed to send message. Please try again.");
        }
    }

    private boolean validateName(String name) {
        return name != null && name.length() >= 2;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneOrEmpty(String phone) {
        // Allow empty phone or valid phone number
        return phone.isEmpty() || phone.matches("^\\+?\\d{10,14}$");
    }

    private void submitMessage(String name, String email, String phone, String message) {
        // Placeholder for actual message submission logic
        System.out.println("Sending message:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        System.out.println("Message: " + message);
    }

    private void showError(String message) {
        statusLabel.setForeground(ERROR_COLOR);
        statusLabel.setText(message);
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        messageArea.setText("");
    }

    // Custom Focus Listener for real-time validation
    private class ValidationFocusListener implements FocusListener {
        private final JTextField field;
        private final Function<String, Boolean> validationFunction;

        public ValidationFocusListener(JTextField field, Function<String, Boolean> validationFunction) {
            this.field = field;
            this.validationFunction = validationFunction;
        }

        @Override
        public void focusGained(FocusEvent e) {
            // Optional: Could add highlight or other visual cue
        }

        @Override
        public void focusLost(FocusEvent e) {
            String text = field.getText().trim();
            if (!text.isEmpty() && !validationFunction.apply(text)) {
                field.setForeground(ERROR_COLOR);
            } else {
                field.setForeground(TEXT_COLOR);
            }
        }
    }

    // Functional interface for validation functions
    @FunctionalInterface
    private interface Function<T, R> {
        R apply(T t);
    }

    public static void main(String[] args) {
        // Ensure UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for a native appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            ContactForm form = new ContactForm();
            form.setVisible(true);
        });
    }
}