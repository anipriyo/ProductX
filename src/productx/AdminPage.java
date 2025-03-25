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
    private static final long serialVersionUID = 1L; // Added to resolve potential serialization warnings
    
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color BG_COLOR = new Color(245, 245, 245);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);

    private Connection con;
    private JTabbedPane tabbedPane;
    private JTable customerTable;
    private JTable inventoryTable;

    public AdminPage(Connection connection) {
        this.con = connection;

        setTitle("ProductX - Admin Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);

        initComponents();

        // Make the frame visible at the end of the constructor
        setVisible(true);
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        // Customer Table Panel
        JPanel customerPanel = createTablePanel("Customer",
                "SELECT CID, Name, Address, Phone_Number, Email FROM Customer");

        // Inventory Table Panel
        JPanel inventoryPanel = createTablePanel("Inventory",
                "SELECT PID, Product_Name, Price, Description, Quantity FROM Inventory");

        tabbedPane.addTab("Customers", customerPanel);
        tabbedPane.addTab("Inventory", inventoryPanel);

        // Action Buttons Panel
        JPanel actionPanel = createActionPanel();

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel(String tableName, String query) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        try {
            Statement stmt = con.createStatement();
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

            // Create table
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Make first column (primary key) non-editable
                    return column != 0;
                }
            };

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

        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(PRIMARY_COLOR);
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> saveChanges());

        JButton addRowButton = new JButton("Add Row");
        addRowButton.setBackground(PRIMARY_COLOR);
        addRowButton.setForeground(Color.WHITE);
        addRowButton.addActionListener(e -> addNewRow());

        JButton deleteRowButton = new JButton("Delete Selected Row");
        deleteRowButton.setBackground(Color.RED);
        deleteRowButton.setForeground(Color.WHITE);
        deleteRowButton.addActionListener(e -> deleteSelectedRow());

        actionPanel.add(saveButton);
        actionPanel.add(addRowButton);
        actionPanel.add(deleteRowButton);

        return actionPanel;
    }

    private void saveChanges() {
    try {
        // Disable auto-commit to manage transactions manually
        con.setAutoCommit(false);

        // Determine which table is currently selected
        JTable currentTable = (tabbedPane.getSelectedIndex() == 0) ? customerTable : inventoryTable;
        DefaultTableModel model = (DefaultTableModel) currentTable.getModel();

        if (tabbedPane.getSelectedIndex() == 0) {
            // Customer table
            PreparedStatement updateStmt = con.prepareStatement(
                    "UPDATE Customer SET Name=?, Address=?, Phone_Number=?, Email=? WHERE CID=?");
            PreparedStatement insertStmt = con.prepareStatement(
                    "INSERT INTO Customer (Name, Address, Phone_Number, Email) VALUES (?, ?, ?, ?)");

            for (int row = 0; row < model.getRowCount(); row++) {
                String cid = (String) model.getValueAt(row, 0);
                String name = (String) model.getValueAt(row, 1);
                String address = (String) model.getValueAt(row, 2);
                String phoneNumber = (String) model.getValueAt(row, 3);
                String email = (String) model.getValueAt(row, 4);

                if (cid == null || cid.trim().isEmpty()) {
                    // New row - insert
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, address);
                    insertStmt.setString(3, phoneNumber);
                    insertStmt.setString(4, email);
                    insertStmt.executeUpdate();
                } else {
                    // Existing row - update
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
            PreparedStatement updateStmt = con.prepareStatement(
                    "UPDATE Inventory SET Product_Name=?, Price=?, Description=?, Quantity=? WHERE PID=?");
            PreparedStatement insertStmt = con.prepareStatement(
                    "INSERT INTO Inventory (Product_Name, Price, Description, Quantity) VALUES (?, ?, ?, ?)");

            for (int row = 0; row < model.getRowCount(); row++) {
                String pid = (String) model.getValueAt(row, 0);
                String productName = (String) model.getValueAt(row, 1);
                double price = Double.parseDouble(model.getValueAt(row, 2).toString());
                String description = (String) model.getValueAt(row, 3);
                int quantity = Integer.parseInt(model.getValueAt(row, 4).toString());

                if (pid == null || pid.trim().isEmpty()) {
                    // New row - insert
                    insertStmt.setString(1, productName);
                    insertStmt.setDouble(2, price);
                    insertStmt.setString(3, description);
                    insertStmt.setInt(4, quantity);
                    insertStmt.executeUpdate();
                } else {
                    // Existing row - update
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
        con.commit();

        // Reset auto-commit to default
        con.setAutoCommit(true);

        JOptionPane.showMessageDialog(this,
                "Changes saved successfully!",
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException e) {
        try {
            // Rollback in case of error
            con.rollback();
        } catch (SQLException rollbackEx) {
            rollbackEx.printStackTrace();
        }

        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Error saving changes: " + e.getMessage(),
                "Save Error",
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
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a row to delete",
                    "No Row Selected",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}