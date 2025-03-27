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
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AdminPage extends JFrame {

    // Constants for UI styling
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);

    // Database connection and UI components
    private Connection con;
    private JTabbedPane tabbedPane;
    private JTable customerTable;
    private JTable inventoryTable;
    private boolean connectionClosed = false;

    public AdminPage(Connection connection) {
        // Validate connection before using
        try {
            if (connection == null || connection.isClosed()) {
                JOptionPane.showMessageDialog(null,
                        "Invalid database connection. Please reconnect.",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.con = connection;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error checking database connection: " + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Frame setup
        setTitle("ProductX - Admin Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);

        // Initialize components
        initComponents();

        // Add window listener to handle connection properly
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                closeConnection();
            }
        });

        // Make the frame visible
        setVisible(true);
    }

    private void closeConnection() {
        if (con != null && !connectionClosed) {
            try {
                con.close();
                connectionClosed = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getValidConnection() throws SQLException {
        // Reopen connection if it's closed
        if (con == null || con.isClosed()) {
            // Use your database connection utility to reconnect
            con = DBConnect.getConnection();
        }
        return con;
    }

    private void initComponents() {
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Customer Table Panel
        JPanel customerPanel = createTablePanel("Customer",
                "SELECT CID, Name, Address, Phone_Number, Email FROM Customer");

        // Inventory Table Panel
        JPanel inventoryPanel = createTablePanel("Inventory",
                "SELECT PID, Product_Name, Price, Description, Quantity FROM Inventory");

        // Add tabs
        tabbedPane.addTab("Customers", customerPanel);
        tabbedPane.addTab("Inventory", inventoryPanel);

        // Create action buttons panel
        JPanel actionPanel = createActionPanel();

        // Set layout and add components
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel(String tableName, String query) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        try {
            // Get a valid connection
            Connection currentCon = getValidConnection();
            Statement stmt = currentCon.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Get column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // Get data
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    row.add(rs.getObject(columnIndex));
                }
                data.add(row);
            }

            // Create table model
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Make first column (primary key) non-editable
                    return column != 0;
                }
            };

            // Create table
            JTable table = new JTable(model);
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            table.setRowHeight(25);

            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Store reference to tables
            if (tableName.equals("Customer")) {
                customerTable = table;
            } else {
                inventoryTable = table;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading " + tableName + " data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(BG_COLOR);

        // Save Changes Button
        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(Color.WHITE);
        saveButton.setForeground(PRIMARY_COLOR);
        saveButton.addActionListener(e -> saveChanges());

        // Add Row Button
        JButton addRowButton = new JButton("Add Row");
        addRowButton.setBackground(Color.WHITE);
        addRowButton.setForeground(PRIMARY_COLOR);
        addRowButton.addActionListener(e -> addNewRow());

        // Delete Row Button
        JButton deleteRowButton = new JButton("Delete Selected Row");
        deleteRowButton.setBackground(Color.WHITE);
        deleteRowButton.setForeground(Color.RED);
        deleteRowButton.addActionListener(e -> deleteSelectedRow());

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(PRIMARY_COLOR);
        backButton.addActionListener(e -> back());

        // Add buttons to panel
        actionPanel.add(saveButton);
        actionPanel.add(addRowButton);
        actionPanel.add(deleteRowButton);
        actionPanel.add(backButton);

        return actionPanel;
    }

    private void saveChanges() {
        try {
            // Get a valid connection
            Connection currentCon = getValidConnection();
            currentCon.setAutoCommit(false);

            // Determine which table is currently selected
            JTable currentTable = (tabbedPane.getSelectedIndex() == 0) ? customerTable : inventoryTable;
            DefaultTableModel model = (DefaultTableModel) currentTable.getModel();

            if (tabbedPane.getSelectedIndex() == 0) {
                // Customer table
                PreparedStatement updateStmt = currentCon.prepareStatement(
                        "UPDATE customer SET Name=?, Address=?, Phone_Number=?, Email=? WHERE CID=?");
                PreparedStatement insertStmt = currentCon.prepareStatement(
                        "INSERT INTO customer (Name, Address, Phone_Number, Email) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);

                for (int row = 0; row < model.getRowCount(); row++) {
                    Object cidObj = model.getValueAt(row, 0);
                    String name = (String) model.getValueAt(row, 1);
                    String address = (String) model.getValueAt(row, 2);
                    String phoneNumber = (String) model.getValueAt(row, 3);
                    String email = (String) model.getValueAt(row, 4);

                    System.out.println("Processing row " + row + ":");
                    System.out.println("CID: " + cidObj);
                    System.out.println("Name: " + name);
                    System.out.println("Address: " + address);
                    System.out.println("Phone: " + phoneNumber);
                    System.out.println("Email: " + email);
                    
                    // Skip rows with empty or null data
                    if (name == null || name.trim().isEmpty()) {
                        continue;
                    }

                    if (cidObj == null || cidObj.toString().trim().isEmpty()) {
                        // New row - insert
                        insertStmt.setString(1, name);
                        insertStmt.setString(2, address);
                        insertStmt.setString(3, phoneNumber);
                        insertStmt.setString(4, email);

                        int affectedRows = insertStmt.executeUpdate();
                        if (affectedRows > 0) {
                            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    // Update the model with the new generated key
                                    model.setValueAt(generatedKeys.getInt(1), row, 0);
                                }
                            }
                        }
                    } else {
                        // Existing row - update
                        String cid = cidObj.toString();
                        updateStmt.setString(1, name);
                        updateStmt.setString(2, address);
                        updateStmt.setString(3, phoneNumber);
                        updateStmt.setString(4, email);
                        updateStmt.setString(5, cid);
                        updateStmt.executeUpdate();
                    }
                }
            } else {
                // Inventory table
                PreparedStatement updateStmt = currentCon.prepareStatement(
                        "UPDATE Inventory SET Product_Name=?, Price=?, Description=?, Quantity=? WHERE PID=?");
                PreparedStatement insertStmt = currentCon.prepareStatement(
                        "INSERT INTO Inventory (Product_Name, Price, Description, Quantity) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);

                for (int row = 0; row < model.getRowCount(); row++) {
                    Object pidObj = model.getValueAt(row, 0);
                    String productName = (String) model.getValueAt(row, 1);

                    // Skip rows with empty or null data
                    if (productName == null || productName.trim().isEmpty()) {
                        continue;
                    }

                    double price = Double.parseDouble(model.getValueAt(row, 2).toString());
                    String description = (String) model.getValueAt(row, 3);
                    int quantity = Integer.parseInt(model.getValueAt(row, 4).toString());

                    if (pidObj == null || pidObj.toString().trim().isEmpty()) {
                        // New row - insert
                        insertStmt.setString(1, productName);
                        insertStmt.setDouble(2, price);
                        insertStmt.setString(3, description);
                        insertStmt.setInt(4, quantity);

                        int affectedRows = insertStmt.executeUpdate();
                        if (affectedRows > 0) {
                            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    // Update the model with the new generated key
                                    model.setValueAt(generatedKeys.getInt(1), row, 0);
                                }
                            }
                        }
                    } else {
                        // Existing row - update
                        String pid = pidObj.toString();
                        updateStmt.setString(1, productName);
                        updateStmt.setDouble(2, price);
                        updateStmt.setString(3, description);
                        updateStmt.setInt(4, quantity);
                        updateStmt.setString(5, pid);
                        updateStmt.executeUpdate();
                    }
                }
            }

            // Commit the transaction
            currentCon.commit();

            // Reset auto-commit to default
            currentCon.setAutoCommit(true);

            JOptionPane.showMessageDialog(this,
                    "Changes saved successfully!",
                    "Save Successful",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }

            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error saving changes: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number format. Please check your input.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewRow() {
        DefaultTableModel model;
        if (tabbedPane.getSelectedIndex() == 0) {
            // Customer table
            model = (DefaultTableModel) customerTable.getModel();
            model.addRow(new Object[]{"", "", "", "", ""});
        } else {
            // Inventory table
            model = (DefaultTableModel) inventoryTable.getModel();
            model.addRow(new Object[]{"", "", 0.0, "", 0});
        }
    }

    private void deleteSelectedRow() {
        JTable currentTable = (tabbedPane.getSelectedIndex() == 0) ? customerTable : inventoryTable;
        DefaultTableModel model = (DefaultTableModel) currentTable.getModel();

        int selectedRow = currentTable.getSelectedRow();
        if (selectedRow != -1) {
            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this row?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Get a valid connection
                    Connection currentCon = getValidConnection();

                    if (tabbedPane.getSelectedIndex() == 0) {
                        // Customer table deletion
                        String cid = model.getValueAt(selectedRow, 0).toString();
                        if (!cid.isEmpty()) {
                            PreparedStatement deleteStmt = currentCon.prepareStatement(
                                    "DELETE FROM Customer WHERE CID = ?");
                            deleteStmt.setString(1, cid);
                            deleteStmt.executeUpdate();
                        }
                    } else {
                        // Inventory table deletion
                        String pid = model.getValueAt(selectedRow, 0).toString();
                        if (!pid.isEmpty()) {
                            PreparedStatement deleteStmt = currentCon.prepareStatement(
                                    "DELETE FROM Inventory WHERE PID = ?");
                            deleteStmt.setString(1, pid);
                            deleteStmt.executeUpdate();
                        }
                    }

                    // Remove row from the table model
                    model.removeRow(selectedRow);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting row: " + e.getMessage(),
                            "Deletion Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to delete",
                    "No Row Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void back() {
        // Close the current connection
        closeConnection();

        // Dispose of the current frame
        dispose();

        // Open the main ProductX page
        SwingUtilities.invokeLater(() -> ProductX.main(new String[]{}));
    }
}
