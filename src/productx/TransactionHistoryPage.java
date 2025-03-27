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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.Vector;

public class TransactionHistoryPage extends JFrame {
    // Theme Color
    private Color primaryColor1 = new Color(70, 130, 180); // Steel blue
    private Color lightPrimaryColor = new Color(100, 160, 210); // Lighter variant
    private Color darkPrimaryColor = new Color(50, 100, 150); // Darker variant

    private Connection con;
    private String cid;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JTextField fromDateField;
    private JTextField toDateField;
    private JTextField productNameField;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JButton filterButton;
    private JButton exportButton;
    private JButton reportButton;
    private JLabel totalSpentLabel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    
    public TransactionHistoryPage(String cid, Connection con, String userType) {
        this.cid = cid;
        this.con = con;

        // Modern look and feel
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Transaction History Dashboard");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Sophisticated gradient background panel
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Gradient using primary color palette
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(240, 248, 255), 
                    getWidth(), getHeight(), primaryColor1.brighter()
                );
                
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Create and add components with modern styling
        mainPanel.add(createFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createSummaryPanel(), BorderLayout.SOUTH);

        // Load initial data
        loadTransactionData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Modern, clean typography
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 12);

        // From Date
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel fromDateLabel = new JLabel("From Date:");
        fromDateLabel.setFont(labelFont);
        fromDateLabel.setForeground(primaryColor1.darker());
        filterPanel.add(fromDateLabel, gbc);

        gbc.gridx = 1;
        fromDateField = createStyledTextField(fieldFont);
        filterPanel.add(fromDateField, gbc);

        // To Date
        gbc.gridx = 2;
        JLabel toDateLabel = new JLabel("To Date:");
        toDateLabel.setFont(labelFont);
        toDateLabel.setForeground(primaryColor1.darker());
        filterPanel.add(toDateLabel, gbc);

        gbc.gridx = 3;
        toDateField = createStyledTextField(fieldFont);
        filterPanel.add(toDateField, gbc);

        // Product Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel productNameLabel = new JLabel("Product Name:");
        productNameLabel.setFont(labelFont);
        productNameLabel.setForeground(primaryColor1.darker());
        filterPanel.add(productNameLabel, gbc);

        gbc.gridx = 1;
        productNameField = createStyledTextField(fieldFont);
        filterPanel.add(productNameField, gbc);

        // Price Range
        gbc.gridx = 2;
        JLabel minPriceLabel = new JLabel("Min Price:");
        minPriceLabel.setFont(labelFont);
        minPriceLabel.setForeground(primaryColor1.darker());
        filterPanel.add(minPriceLabel, gbc);

        gbc.gridx = 3;
        minPriceField = createStyledTextField(fieldFont);
        filterPanel.add(minPriceField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel maxPriceLabel = new JLabel("Max Price:");
        maxPriceLabel.setFont(labelFont);
        maxPriceLabel.setForeground(primaryColor1.darker());
        filterPanel.add(maxPriceLabel, gbc);

        gbc.gridx = 3;
        maxPriceField = createStyledTextField(fieldFont);
        filterPanel.add(maxPriceField, gbc);

        // Modern, flat design buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        filterButton = createFlatButton("Filter Transactions", primaryColor1, darkPrimaryColor);
        filterButton.addActionListener(e -> filterTransactions());
        filterPanel.add(filterButton, gbc);

        gbc.gridx = 2;
        exportButton = createFlatButton("Export to CSV", lightPrimaryColor, primaryColor1);
        exportButton.addActionListener(e -> exportToCSV());
        filterPanel.add(exportButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        reportButton = createFlatButton("Generate Detailed Report", 
            primaryColor1.brighter(), primaryColor1);
        reportButton.addActionListener(e -> generateReport());
        filterPanel.add(reportButton, gbc);

        return filterPanel;
    }

    private JScrollPane createTablePanel() {
        // Table setup with enhanced styling
        String[] columnNames = {"Buy ID", "Product ID", "Product Name", 
                                "Price", "Buyer ID", "Seller ID", 
                                "Quantity", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);

        // Modern table styling
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transactionTable.setRowHeight(30);
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        transactionTable.getTableHeader().setBackground(primaryColor1);
        transactionTable.getTableHeader().setForeground(Color.WHITE);

        // Enhanced alternating row colors with soft gradient
        transactionTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                                                           boolean isSelected, boolean hasFocus, 
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                                                                  isSelected, hasFocus, 
                                                                  row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? 
                        new Color(240, 248, 255) : 
                        new Color(230, 240, 250)
                    );
                }
                return c;
            }
        });

        // Enable sorting
        rowSorter = new TableRowSorter<>(tableModel);
        transactionTable.setRowSorter(rowSorter);

        // Scroll pane with modern border
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 10, 0),
            BorderFactory.createLineBorder(primaryColor1.brighter(), 1)
        ));

        return scrollPane;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        summaryPanel.setOpaque(false);
        
        totalSpentLabel = new JLabel("Total Spent: $0.00");
        totalSpentLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalSpentLabel.setForeground(primaryColor1.darker());
        
        summaryPanel.add(totalSpentLabel);

        return summaryPanel;
    }

    // Improved styled text field
    private JTextField createStyledTextField(Font font) {
        JTextField textField = new JTextField(15);
        textField.setFont(font);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor1.brighter(), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        textField.setBackground(Color.WHITE);
        return textField;
    }

    // Modern flat design button
    private JButton createFlatButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        
        // Hover and click effects
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(baseColor);
            }
            public void mousePressed(MouseEvent evt) {
                button.setBackground(hoverColor.darker());
            }
            public void mouseReleased(MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });

        return button;
    }

    // Existing methods (loadTransactionData, filterTransactions, 
    // exportToCSV, generateReport) remain the same
    // ... [Rest of the code remains the same as in the previous implementation]

    // Previous methods (loadTransactionData, filterTransactions, 
    // exportToCSV, generateReport) remain the same as in the previous implementation

    // Main method for testing

    private void loadTransactionData() {
        // Clear existing rows
        tableModel.setRowCount(0);

        try {
            String query = "SELECT * FROM buy_record WHERE Buyer_CID = ? ORDER BY Date DESC";
            
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, cid);
            
            ResultSet rs = pst.executeQuery();
            
            double totalSpent = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("Buy_ID"));
                row.add(rs.getString("PID"));
                row.add(rs.getString("Product_Name"));
                row.add(rs.getBigDecimal("Buying_Price"));
                row.add(rs.getString("Buyer_CID"));
                row.add(rs.getString("Seller_CID"));
                row.add(rs.getInt("Quantity"));
                row.add(rs.getTimestamp("Date"));
                
                tableModel.addRow(row);
                
                // Calculate total spent
                double rowTotal = rs.getBigDecimal("Buying_Price").doubleValue() * 
                                  rs.getInt("Quantity");
                totalSpent += rowTotal;
            }
            
            // Update total spent label
            totalSpentLabel.setText(String.format("Total Spent: $%.2f", totalSpent));
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading transaction data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterTransactions() {
        String fromDate = fromDateField.getText().trim();
        String toDate = toDateField.getText().trim();
        String productName = productNameField.getText().trim();
        String minPrice = minPriceField.getText().trim();
        String maxPrice = maxPriceField.getText().trim();

        // Clear existing rows
        tableModel.setRowCount(0);

        try {
            StringBuilder queryBuilder = new StringBuilder(
                "SELECT * FROM buy_record WHERE Buyer_CID = ? ");
            
            // Dynamic filter conditions
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                queryBuilder.append("AND Date BETWEEN ? AND ? ");
            }
            
            if (!productName.isEmpty()) {
                queryBuilder.append("AND Product_Name LIKE ? ");
            }
            
            if (!minPrice.isEmpty() && !maxPrice.isEmpty()) {
                queryBuilder.append("AND Buying_Price BETWEEN ? AND ? ");
            }
            
            queryBuilder.append("ORDER BY Date DESC");
            
            PreparedStatement pst = con.prepareStatement(queryBuilder.toString());
            
            // Set parameters dynamically
            int paramIndex = 1;
            pst.setString(paramIndex++, cid);
            
            if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                pst.setString(paramIndex++, fromDate);
                pst.setString(paramIndex++, toDate);
            }
            
            if (!productName.isEmpty()) {
                pst.setString(paramIndex++, "%" + productName + "%");
            }
            
            if (!minPrice.isEmpty() && !maxPrice.isEmpty()) {
                pst.setDouble(paramIndex++, Double.parseDouble(minPrice));
                pst.setDouble(paramIndex++, Double.parseDouble(maxPrice));
            }
            
            ResultSet rs = pst.executeQuery();
            
            double totalSpent = 0;
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("Buy_ID"));
                row.add(rs.getString("PID"));
                row.add(rs.getString("Product_Name"));
                row.add(rs.getBigDecimal("Buying_Price"));
                row.add(rs.getString("Buyer_CID"));
                row.add(rs.getString("Seller_CID"));
                row.add(rs.getInt("Quantity"));
                row.add(rs.getTimestamp("Date"));
                
                tableModel.addRow(row);
                
                // Calculate total spent
                double rowTotal = rs.getBigDecimal("Buying_Price").doubleValue() * 
                                  rs.getInt("Quantity");
                totalSpent += rowTotal;
            }
            
            // Update total spent label
            totalSpentLabel.setText(String.format("Total Spent: $%.2f", totalSpent));
            
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Error filtering transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                // Write headers
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.print(tableModel.getColumnName(i) + 
                        (i < tableModel.getColumnCount() - 1 ? "," : "\n"));
                }
                
                // Write data
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        writer.print(tableModel.getValueAt(row, col) + 
                            (col < tableModel.getColumnCount() - 1 ? "," : "\n"));
                    }
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Data exported successfully to " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting data: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void generateReport() {
        // Create a simple report dialog
        JDialog reportDialog = new JDialog(this, "Transaction Report", true);
        reportDialog.setLayout(new BorderLayout());
        reportDialog.setSize(600, 400);

        // Summary Panel
        JPanel summaryPanel = new JPanel(new GridLayout(0, 2));
        
        // Aggregate calculations
        try {
            String summaryQuery = "SELECT " +
                "COUNT(*) AS Total_Transactions, " +
                "SUM(Quantity) AS Total_Items, " +
                "SUM(Buying_Price * Quantity) AS Total_Spent, " +
                "AVG(Buying_Price) AS Avg_Price, " +
                "MAX(Buying_Price) AS Max_Price, " +
                "MIN(Buying_Price) AS Min_Price " +
                "FROM buy_record WHERE Buyer_CID = ?";
            
            PreparedStatement pst = con.prepareStatement(summaryQuery);
            pst.setString(1, cid);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                summaryPanel.add(new JLabel("Total Transactions:"));
                summaryPanel.add(new JLabel(rs.getString("Total_Transactions")));
                
                summaryPanel.add(new JLabel("Total Items Purchased:"));
                summaryPanel.add(new JLabel(rs.getString("Total_Items")));
                
                summaryPanel.add(new JLabel("Total Spent:"));
                summaryPanel.add(new JLabel(String.format("$%.2f", 
                    rs.getDouble("Total_Spent"))));
                
                summaryPanel.add(new JLabel("Average Price:"));
                summaryPanel.add(new JLabel(String.format("$%.2f", 
                    rs.getDouble("Avg_Price"))));
                
                summaryPanel.add(new JLabel("Highest Price:"));
                summaryPanel.add(new JLabel(String.format("$%.2f", 
                    rs.getDouble("Max_Price"))));
                
                summaryPanel.add(new JLabel("Lowest Price:"));
                summaryPanel.add(new JLabel(String.format("$%.2f", 
                    rs.getDouble("Min_Price"))));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error generating report: " + e.getMessage());
            e.printStackTrace();
        }

        // Product Breakdown Panel
        JTextArea productBreakdown = new JTextArea();
        productBreakdown.setEditable(false);
        
        try {
            String productQuery = "SELECT Product_Name, " +
                "COUNT(*) AS Purchase_Count, " +
                "SUM(Quantity) AS Total_Quantity, " +
                "SUM(Buying_Price * Quantity) AS Total_Spent " +
                "FROM buy_record WHERE Buyer_CID = ? " +
                "GROUP BY Product_Name " +
                "ORDER BY Total_Spent DESC";
            
            PreparedStatement pst = con.prepareStatement(productQuery);
            pst.setString(1, cid);
            
            ResultSet rs = pst.executeQuery();
            
            productBreakdown.append("Product Breakdown:\n");
            productBreakdown.append("Product Name\tPurchase Count\tTotal Quantity\tTotal Spent\n");
            productBreakdown.append("-----------------------------------------------------------\n");
            
            while (rs.next()) {
                productBreakdown.append(String.format("%s\t%d\t%d\t$%.2f\n", 
                    rs.getString("Product_Name"),
                    rs.getInt("Purchase_Count"),
                    rs.getInt("Total_Quantity"),
                    rs.getDouble("Total_Spent")
                ));
            }
        } catch (SQLException e) {
            productBreakdown.append("Error generating product breakdown: " + e.getMessage());
        }

        // Add components to dialog
        reportDialog.add(summaryPanel, BorderLayout.NORTH);
        reportDialog.add(new JScrollPane(productBreakdown), BorderLayout.CENTER);
        
        reportDialog.setLocationRelativeTo(this);
        reportDialog.setVisible(true);
    }

    // Optional: Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            try {
//                // Replace with your actual database connection details
//                Connection testCon = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/yourdatabase", 
//                    "username", 
//                    "password"
//                );
//                new TransactionHistoryPage("testCID", testCon, "buyer");
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, 
//                    "Database connection error: " + e.getMessage());
//            }
        });
    }
}

