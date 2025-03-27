/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productx;

import javax.swing.table.DefaultCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class MainPage {

    private JLabel cartCountLabel;
    private JButton cartButton;
    private JFrame frame;
    private String cid;
    private Connection con;
    private JLabel lblUserInfo;
    private JPanel productPanel;
    private JTextField searchField;
    private JButton searchButton;
    private Color primaryColor1 = new Color(70, 130, 180); // Steel blue
    private Color accentColor = new Color(211, 211, 211); // Light gray
    private Color textColor = new Color(33, 33, 33); // Dark gray for text
    private Font headerFont = new Font("Arial", Font.BOLD, 16);
    private Font normalFont = new Font("Arial", Font.PLAIN, 14);

    public MainPage(String cid, Connection con) {
        this.cid = cid;
        this.con = con;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("ProductX - Home");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.WHITE);

        // Debug: Check CID value
        System.out.println("Opening MainPage with CID: " + cid);

        // Fetch user name
        String userName = getUserName();
        System.out.println("Fetched User Name: " + userName); // Debugging

        // **HEADER PANEL**
        JPanel headerPanel = createHeaderPanel(userName);

        // **NAVIGATION PANEL**
        JPanel navPanel = createNavigationPanel();

        // **SEARCH PANEL**
        JPanel searchPanel = createSearchPanel();

        // **PRODUCT DISPLAY PANEL**
        JScrollPane scrollPane = createProductPanel();

        // **FOOTER PANEL**
        JPanel footerPanel = createFooterPanel();

        // Load products
        loadProducts("");

        // **ADDING COMPONENTS TO FRAME**
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(navPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createHeaderPanel(String userName) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor1);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Logo and welcome message
        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftHeader.setBackground(primaryColor1);
        JLabel logoLabel = new JLabel("ProductX");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);

        lblUserInfo = new JLabel("Welcome, " + userName);
        lblUserInfo.setFont(normalFont);
        lblUserInfo.setForeground(Color.WHITE);

        leftHeader.add(logoLabel);
        leftHeader.add(Box.createHorizontalStrut(20));
        leftHeader.add(lblUserInfo);

        // Profile, cart and logout buttons
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightHeader.setBackground(primaryColor1);

        // Add cart button
        cartButton = createStyledButton("Cart", primaryColor1, Color.WHITE);
        cartCountLabel = new JLabel("0");
        cartCountLabel.setFont(new Font("Arial", Font.BOLD, 12));
        cartCountLabel.setForeground(Color.WHITE);
        cartCountLabel.setBackground(Color.RED);
        cartCountLabel.setOpaque(true);
        cartCountLabel.setBorder(new EmptyBorder(2, 5, 2, 5));
        cartCountLabel.setVisible(false);

        JPanel cartPanel = new JPanel(new BorderLayout());
//        cartPanel.setBackground(Color.WHITE);
//        cartPanel.setForeground(primaryColor1);
        cartPanel.add(cartButton, BorderLayout.CENTER);
        cartPanel.add(cartCountLabel, BorderLayout.EAST);

        cartButton.addActionListener(e -> openCartCheckout());

        JButton btnProfile = createStyledButton("Profile", primaryColor1, Color.WHITE);
        JButton btnLogout = createStyledButton("Logout", primaryColor1, Color.WHITE);

        btnProfile.addActionListener(e -> openProfilePage());
        btnLogout.addActionListener(e -> logout());

        rightHeader.add(cartPanel);
        rightHeader.add(btnProfile);
        rightHeader.add(btnLogout);

        headerPanel.add(leftHeader, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, accentColor));
        navPanel.setPreferredSize(new Dimension(180, 0));

        // Navigation title
        JLabel navTitle = new JLabel("Navigation");
        navTitle.setFont(headerFont);
        navTitle.setForeground(textColor);
        navTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        navTitle.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Create navigation buttons
        JButton btnHome = createMenuButton("Home", true);
        JButton btnInventory = createMenuButton("My Inventory", false);
        JButton btnOrders = createMenuButton("My Orders", false);
        JButton btnSales = createMenuButton("My Sales", false);
        JButton btnSettings = createMenuButton("ContactUS", false);

        // Add action listeners
        btnHome.addActionListener(e -> {
            // Already on home page
        });
        btnInventory.addActionListener(e -> openInventoryPage());
        btnOrders.addActionListener(e -> showPurchaseHistory());
        btnSales.addActionListener(e -> showSaleHistory());
        btnSettings.addActionListener(e -> contact());

        // Add components to nav panel
        navPanel.add(navTitle);
        navPanel.add(btnHome);
        navPanel.add(btnInventory);
        navPanel.add(btnOrders);
        navPanel.add(btnSales);
        navPanel.add(btnSettings);
        navPanel.add(Box.createVerticalGlue());

        return navPanel;
    }

    public void updateCartIndicator() {
        Cart cart = Cart.getInstance();
        int itemCount = cart.getItemCount();

        if (itemCount > 0) {
            cartCountLabel.setText(String.valueOf(itemCount));
            cartCountLabel.setVisible(true);
        } else {
            cartCountLabel.setVisible(false);
        }
    }

    private void openCartCheckout() {
        Cart cart = Cart.getInstance();

        if (cart.getItemCount() == 0) {
            JOptionPane.showMessageDialog(frame, "Your cart is empty",
                    "Cart", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Open checkout page with all cart items
        new CheckoutBuy(cid, cart.getItems(), con);

        // This will be updated from CheckoutBuy if purchase completes
        updateCartIndicator();
    }

    private JButton createMenuButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(normalFont);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setFocusPainted(false);

        if (selected) {
            button.setBackground(accentColor);
            button.setForeground(primaryColor1);
            button.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, primaryColor1));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(textColor);
            button.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.WHITE));
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!selected) {
                    button.setBackground(new Color(245, 245, 245));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!selected) {
                    button.setBackground(Color.WHITE);
                }
            }
        });

        return button;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        searchField = new JTextField(20);
        searchField.setFont(normalFont);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        searchButton = createStyledButton("Search", Color.BLUE, primaryColor1);
        searchButton.addActionListener(e -> loadProducts(searchField.getText()));

        JButton clearButton = createStyledButton("Clear/Refresh", primaryColor1, Color.WHITE);
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadProducts("");
            frame.dispose();
            initializeUI(); // hihi
        });

        searchPanel.add(new JLabel("Search Products: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
//        searchPanel.add(refreshButton);

        return searchPanel;
    }

    private JScrollPane createProductPanel() {
        productPanel = new JPanel(new GridBagLayout());
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // Smooth scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(50);

        return scrollPane;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, accentColor));

        // Add search panel at the top of footer
        footerPanel.add(createSearchPanel(), BorderLayout.NORTH);

        // Add status and copyright at the bottom
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(new EmptyBorder(5, 15, 5, 15));

        JLabel statusLabel = new JLabel("© 2025 ProductX. All rights reserved.");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(120, 120, 120));

        statusPanel.add(statusLabel, BorderLayout.WEST);
        footerPanel.add(statusPanel, BorderLayout.SOUTH);

        return footerPanel;
    }

    private JButton createStyledButton(String text, Color textColor, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(normalFont);
        button.setForeground(textColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker()),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private String getUserName() {
        try {
            String query = "SELECT name FROM customer WHERE cid = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, cid);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            } else {
                System.out.println("No user found with CID: " + cid); // Debugging
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown User";
    }

    private void showSaleHistory() {
        new SalesHistoryPage(cid, con, "seller");
    }

    private void showPurchaseHistory() {
        new TransactionHistoryPage(cid, con, "buyer");
    }

    private void openInventoryPage() {
        new InventoryPage(cid, con);
    }

    private void openProfilePage() {
        new ProfilePage(cid, con);
    }

    private void contact() {
        SwingUtilities.invokeLater(() -> {
            ContactForm form = new ContactForm();
            form.setVisible(true);
        });
    }

    private void logout() {
        int response = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            frame.dispose(); // Close the MainPage window
            SwingUtilities.invokeLater(() -> ProductX.main(new String[]{})); // Restart the application
        }
    }

    protected void loadProducts(String searchQuery) {
        try {
            String query = "SELECT i.product_name, i.price, i.quantity, c.name as seller_name "
                    + "FROM inventory i JOIN customer c ON i.cid = c.cid "
                    + "WHERE i.product_name LIKE ? AND i.quantity > 0";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, "%" + searchQuery + "%");
            ResultSet rs = pst.executeQuery();

            productPanel.removeAll(); // Clear previous products
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(10, 10, 10, 10); // Add padding between cards
            gbc.anchor = GridBagConstraints.WEST;

            // Add header to product panel
            if (searchQuery != null && !searchQuery.isEmpty()) {
                JLabel searchResultLabel = new JLabel("Search results for: " + searchQuery);
                searchResultLabel.setFont(headerFont);
                searchResultLabel.setForeground(textColor);
                searchResultLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                searchResultLabel.setBorder(new EmptyBorder(0, 0, 15, 0));

                JPanel headerWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
                headerWrapper.setBackground(Color.WHITE);
                headerWrapper.add(searchResultLabel);
                headerWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                productPanel.add(headerWrapper);
            }

            if (searchQuery != null && !searchQuery.isEmpty()) {
                ///////////////////
                JLabel searchResultLabel = new JLabel("Search results for: " + searchQuery);
                searchResultLabel.setFont(headerFont);
                gbc.gridwidth = GridBagConstraints.REMAINDER; // Span entire width
                productPanel.add(searchResultLabel, gbc);
                gbc.gridy++;
                gbc.gridwidth = 1; // Reset to default
            }

            int productsFound = 0;
            while (rs.next()) {
                String productName = rs.getString("product_name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String sellerName = rs.getString("seller_name");

                // Create product card
                JPanel card = createProductCard(productName, price, quantity, sellerName);

                // Add card to grid
                if (gbc.gridx >= 3) { // Reset to next row after 3 cards
                    gbc.gridx = 0;
                    gbc.gridy++;
                }

                productPanel.add(card, gbc);
                gbc.gridx++;
                productsFound++;
            }

            // Show "no products found" message if necessary
            if (productsFound == 0) {
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                JLabel noProductsLabel = new JLabel("No products found"
                        + (searchQuery.isEmpty() ? "." : " matching '" + searchQuery + "'."));
                noProductsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                noProductsLabel.setForeground(new Color(120, 120, 120));
                productPanel.add(noProductsLabel, gbc);
            }

            productPanel.revalidate();
            productPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading products: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createProductCard(String productName, double price, int quantity, String sellerName) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(220, 220));
        card.setMaximumSize(new Dimension(220, 220));
        card.setMinimumSize(new Dimension(220, 220));

        // Product name label
        JLabel lblProductName = new JLabel(productName);
        lblProductName.setFont(new Font("Arial", Font.BOLD, 16));
        lblProductName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Price label with currency symbol
        JLabel lblPrice = new JLabel(String.format("₹%.2f", price));
        lblPrice.setFont(new Font("Arial", Font.BOLD, 18));
        lblPrice.setForeground(new Color(46, 125, 50));  // Green color for price
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Availability label
        JLabel lblStock = new JLabel("In stock: " + quantity);
        lblStock.setFont(new Font("Arial", Font.PLAIN, 12));
        lblStock.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Seller information
        JLabel lblSeller = new JLabel("Sold by: " + sellerName);
        lblSeller.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSeller.setForeground(new Color(100, 100, 100));
        lblSeller.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Divider
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(200, 1));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buy button
        JButton btnBuy = new JButton("Buy Now");
        btnBuy.setBackground(primaryColor1);
        btnBuy.setForeground(Color.BLUE);
        btnBuy.setFocusPainted(false);
        btnBuy.addActionListener(e -> handleBuyProduct(productName, price, sellerName));

        // Add to Cart button
        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setBackground(primaryColor1);
        btnAddToCart.setForeground(Color.RED);
//        btnAddToCart.setBorder(BorderFactory.createLineBorder(primaryColor1));
        btnAddToCart.setFocusPainted(false);
        btnAddToCart.addActionListener(e -> handleAddToCart(productName, price, sellerName));

        buttonPanel.add(btnBuy);
        buttonPanel.add(btnAddToCart);

        // Add components to card with some spacing
        card.add(lblProductName);
        card.add(Box.createVerticalStrut(5));
        card.add(lblPrice);
        card.add(Box.createVerticalStrut(3));
        card.add(lblStock);
        card.add(Box.createVerticalStrut(3));
        card.add(lblSeller);
        card.add(Box.createVerticalStrut(10));
        card.add(separator);
        card.add(Box.createVerticalStrut(10));
        card.add(buttonPanel);

        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(primaryColor1, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accentColor, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return card;
    }

    private void handleBuyProduct(String productName, double price, String sellerName) {
        try {
            // Query inventory table to get PID and SellerID (CID) directly
            String productQuery = "SELECT i.PID, i.CID AS SellerID FROM Inventory i "
                    + "JOIN Customer c ON i.CID = c.CID "
                    + "WHERE i.Product_Name = ? AND c.Name = ?";

            PreparedStatement productPst = con.prepareStatement(productQuery);
            productPst.setString(1, productName);
            productPst.setString(2, sellerName);
            ResultSet productRs = productPst.executeQuery();

            if (productRs.next()) {
                String pid = productRs.getString("PID");
                String sellerCid = productRs.getString("SellerID");

                // Use the current user's CID (cid) as BuyerID
                // Redirect to CheckoutBuy page with seller ID, buyer ID (current user), and product ID
                new CheckoutBuy(sellerCid, cid, pid, con);
            } else {
                JOptionPane.showMessageDialog(frame, "Product or seller information could not be found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            loadProducts("");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error processing request: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recordPurchase(String productName, String sellerCid, int quantity) throws SQLException {
        String insertQuery = "INSERT INTO buy_record (product_name, seller_cid, buyer_cid, quantity) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(insertQuery);
        pst.setString(1, productName);
        pst.setString(2, sellerCid);
        pst.setString(3, cid);
        pst.setInt(4, quantity);
        pst.executeUpdate();
    }

    private void updateInventory(String sellerCid, String productName, int quantity) throws SQLException {
        String updateQuery = "UPDATE inventory SET quantity = quantity - ? WHERE cid = ? AND product_name = ?";
        PreparedStatement pst = con.prepareStatement(updateQuery);
        pst.setInt(1, quantity);
        pst.setString(2, sellerCid);
        pst.setString(3, productName);
        pst.executeUpdate();
    }

    private void handleAddToCart(String productName, double price, String sellerName) {
        try {
            // Query inventory table to get PID and SellerID (CID) directly
            String productQuery = "SELECT i.PID, i.CID AS SellerID, i.Quantity FROM Inventory i "
                    + "JOIN Customer c ON i.CID = c.CID "
                    + "WHERE i.Product_Name = ? AND c.Name = ?";

            PreparedStatement productPst = con.prepareStatement(productQuery);
            productPst.setString(1, productName);
            productPst.setString(2, sellerName);
            ResultSet productRs = productPst.executeQuery();

            if (productRs.next()) {
                String pid = productRs.getString("PID");
                String sellerCid = productRs.getString("SellerID");
                int availableQuantity = productRs.getInt("Quantity");

                // Create product item
                CheckoutBuy.ProductItem item = new CheckoutBuy.ProductItem(
                        pid, sellerCid, productName, price, 1, availableQuantity);

                // Add to cart
                Cart cart = Cart.getInstance();
                cart.addItem(pid, item);

                // Update cart indicator on UI
                updateCartIndicator();

                JOptionPane.showMessageDialog(frame,
                        productName + " added to your cart! (Total items: " + cart.getItemCount() + ")",
                        "Added to Cart", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Product information could not be found.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding to cart: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Custom FlowLayout subclass that provides proper wrapping of components
    class WrapLayout extends FlowLayout {

        public WrapLayout(int align, int hgap, int vgap) {
            super(align, hgap, vgap);
        }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            return layoutSize(target, false);
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int targetWidth = target.getSize().width;

                if (targetWidth == 0) {
                    targetWidth = Integer.MAX_VALUE;
                }

                int hgap = getHgap();
                int vgap = getVgap();
                Insets insets = target.getInsets();
                int horizontalInsetsAndGap = insets.left + insets.right + (hgap * 2);
                int maxWidth = targetWidth - horizontalInsetsAndGap;

                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0;
                int rowHeight = 0;

                int nmembers = target.getComponentCount();

                for (int i = 0; i < nmembers; i++) {
                    Component m = target.getComponent(i);

                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();

                        if (rowWidth + d.width > maxWidth) {
                            addRow(dim, rowWidth, rowHeight);
                            rowWidth = 0;
                            rowHeight = 0;
                        }

                        if (rowWidth != 0) {
                            rowWidth += hgap;
                        }

                        rowWidth += d.width;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }

                addRow(dim, rowWidth, rowHeight);

                dim.width += horizontalInsetsAndGap;
                dim.height += insets.top + insets.bottom + vgap * 2;

                return dim;
            }
        }

        private void addRow(Dimension dim, int rowWidth, int rowHeight) {
            dim.width = Math.max(dim.width, rowWidth);

            if (dim.height > 0) {
                dim.height += getVgap();
            }

            dim.height += rowHeight;
        }
    }
}
