package com.friendlycafe.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.friendlycafe.controller.CafeController;
import com.friendlycafe.exception.CustomerFoundException;
import com.friendlycafe.exception.InvalidMailFormatException;
import com.friendlycafe.pojo.Beverage;
import com.friendlycafe.pojo.Dessert;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.service.DataService;
import com.friendlycafe.service.LogService;

public class FriendlyCafe {
    // Colour palette
    private static final Color DARK_RED = new Color(139, 0, 0);
    private static final Color MEDIUM_RED = new Color(200, 0, 0);
    private static final Color LIGHT_RED = new Color(255, 153, 153);
    private static final Color VERY_LIGHT_RED = new Color(255, 204, 204);
    private static final Color ACCENT_RED = new Color(220, 20, 60);
    
    // Services
    private DataService dataService;
    private CafeController cafeController;
    private LogService logService;

    // UI Components
    private JFrame parentFrame;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel itemDisplayPanel;
    private JPanel cartPanel;
    
    // Data
    private String customerName = "";
    private String customerEmail = "";
    private HashMap<String, Integer> orderingItems = new HashMap<>();
    private double totalCost = 0.0;
    private double discountedCost = 0.0;
    private ArrayList<Item> menuItems;
    private HashMap<String, Item> menuItemsMap = new HashMap<>();
    
    // Price format
    private DecimalFormat currencyFormat = new DecimalFormat("£#,##0.00");
    
    public FriendlyCafe() {
        this(null);
    }

    
    public FriendlyCafe(JFrame parentFrame) {
        dataService = new DataService();
        cafeController = new CafeController();
        logService = LogService.getInstance();
      
        this.parentFrame = parentFrame;

        loadMenu();
        
        setupUI();
    }
    
    private void loadMenu() {
        try {
            menuItems = dataService.getMenu();
            logService.log("Menui Items size : "+ menuItems.size());
            
	            if (menuItems == null || menuItems.isEmpty()) {

            } else {
                logService.log("Successfully loaded " + menuItems.size() + " menu items");
                for (Item item : menuItems) {
                    menuItemsMap.put(item.itemId, item);
                }
            }
            if (menuItems != null && !menuItems.isEmpty()) {
                for (int i = 0; i < Math.min(3, menuItems.size()); i++) {
                    Item item = menuItems.get(i);
                    logService.log("Sample item " + i + ": " + item.itemId + " - " + item.name);
                }
            }
        } catch (Exception e) {
            logService.log("ERROR loading menu: " + e.getMessage());
            e.printStackTrace();
            menuItems = new ArrayList<>();
        }
    }
    
    // GUI
    protected void setupUI() {
        frame = new JFrame("Friendly Cafe");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(960, 600);
        frame.setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        JPanel homeScreen = createHomeScreen();
        JPanel orderScreen = createOrderScreen();
        JPanel paymentScreen = createPaymentScreen();
        JPanel billScreen = createBillScreen();

        mainPanel.add(homeScreen, "HOME");
        mainPanel.add(orderScreen, "ORDER");
        mainPanel.add(paymentScreen, "PAYMENT");
        mainPanel.add(billScreen, "BILL");
 
        frame.getContentPane().add(mainPanel);

        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logService.log("FriendlyCafe GUI closed");
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
            }
        });
    }
    
    private JPanel createHomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(VERY_LIGHT_RED);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(MEDIUM_RED);
        headerPanel.setPreferredSize(new Dimension(300, 100));
        JLabel titleLabel = new JLabel("Welcome to Friendly Cafe");

        TitledBorder titledBorder = BorderFactory.createTitledBorder(" ");

        EmptyBorder paddingBorder = new EmptyBorder(10, 10, 10, 10);

        CompoundBorder compoundBorder = BorderFactory.createCompoundBorder(
            titledBorder,
            paddingBorder
        );
        
        headerPanel.setBorder(compoundBorder);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(VERY_LIGHT_RED);
        
        JLabel welcomeLabel = new JLabel("Delicious Food & Beverages");
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        welcomeLabel.setForeground(DARK_RED);
        
        JButton showMenuButton = createStyledButton("Show Menu", MEDIUM_RED, Color.BLACK);
        showMenuButton.setBorder(BorderFactory.createCompoundBorder(
	               BorderFactory.createLineBorder(DARK_RED, 3, true),
	               BorderFactory.createEmptyBorder(2, 7, 2, 7)
	           ));
        showMenuButton.setPreferredSize(new Dimension(200, 50));
        showMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "ORDER"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        centerPanel.add(welcomeLabel, gbc);
        
        gbc.gridy = 1;
        centerPanel.add(showMenuButton, gbc);
        
        panel.add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(MEDIUM_RED);
        footerPanel.setPreferredSize(new Dimension(900, 50));
        
        JLabel footerLabel = new JLabel("© 2025 Friendly Cafe");
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        
        panel.add(footerPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Order screen
    private JPanel createOrderScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // top menu bar with category buttons
        JPanel topMenuBar = new JPanel(new GridLayout(1, 3));
        topMenuBar.setBackground(MEDIUM_RED);
        topMenuBar.setPreferredSize(new Dimension(900, 50));
        
        JButton foodButton = createStyledButton("Food", DARK_RED, Color.BLACK);
        JButton beverageButton = createStyledButton("Beverages", DARK_RED, Color.BLACK);
        JButton dessertButton = createStyledButton("Desserts", DARK_RED, Color.BLACK);
        
        topMenuBar.add(foodButton);
        topMenuBar.add(beverageButton);
        topMenuBar.add(dessertButton);
        
        panel.add(topMenuBar, BorderLayout.NORTH);
        
        // bottom buttons panel
        JPanel bottomButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomButtonsPanel.setBackground(LIGHT_RED);
        bottomButtonsPanel.setPreferredSize(new Dimension(900, 50));
        
        JButton homeButton = createStyledButton("Back", DARK_RED, Color.BLACK);
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "HOME"));
        
        JButton placeOrderButton = createStyledButton("Place Order", DARK_RED, Color.BLACK);
        placeOrderButton.setBorder(BorderFactory.createCompoundBorder(
	               BorderFactory.createLineBorder(DARK_RED, 3, true),
	               BorderFactory.createEmptyBorder(2, 7, 2, 7)
	           ));        
        placeOrderButton.addActionListener(e -> {
             if (orderingItems.isEmpty()) {
                 JOptionPane.showMessageDialog(frame, "Please add items to your cart first", 
                         "Empty Cart", JOptionPane.WARNING_MESSAGE);
             } else 
                 cardLayout.show(mainPanel, "PAYMENT");
        });
        
        bottomButtonsPanel.add(homeButton);
        bottomButtonsPanel.add(placeOrderButton);
        
        panel.add(bottomButtonsPanel, BorderLayout.SOUTH);
        
        // main content panel with split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setEnabled(false);
        
        // Left panel for displaying menu items
        itemDisplayPanel = new JPanel(new BorderLayout());
        itemDisplayPanel.setBackground(VERY_LIGHT_RED);
        JScrollPane itemScrollPane = new JScrollPane(itemDisplayPanel);
        itemScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Right panel for cart
        cartPanel = createCartPanel();
        JScrollPane cartScrollPane = new JScrollPane(cartPanel);
        cartScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        splitPane.setLeftComponent(itemScrollPane);
        splitPane.setRightComponent(cartScrollPane);
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        foodButton.addActionListener(e -> populateItemDisplay(getItemsByCategory("Food")));
        beverageButton.addActionListener(e -> populateItemDisplay(getItemsByCategory("Beverage")));
        dessertButton.addActionListener(e -> populateItemDisplay(getItemsByCategory("Dessert")));

        foodButton.doClick();
        
        return panel;
    }
    
    // cart panel
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Cart header
        JLabel cartHeader = new JLabel("Cart", SwingConstants.CENTER);
        cartHeader.setFont(new Font("Arial", Font.BOLD, 20));
        cartHeader.setForeground(DARK_RED);
        cartHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        panel.add(cartHeader, BorderLayout.NORTH);
        
        // Cart items panel
        JPanel cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(VERY_LIGHT_RED);
        
        JScrollPane cartScrollPane = new JScrollPane(cartItemsPanel);
        cartScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(cartScrollPane, BorderLayout.NORTH);
        
        // Cart summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBackground(LIGHT_RED);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel subtotalLabel = new JLabel("Subtotal: " + currencyFormat.format(0.0));
        subtotalLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtotalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel discountLabel = new JLabel("Discount: " + currencyFormat.format(0.0));
        discountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        discountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel totalLabel = new JLabel("Total: " + currencyFormat.format(0.0));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(DARK_RED);
        totalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        summaryPanel.add(subtotalLabel);
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(discountLabel);
        summaryPanel.add(Box.createVerticalStrut(5));
        summaryPanel.add(totalLabel);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // payment screen
    private JPanel createPaymentScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(VERY_LIGHT_RED);
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_RED);
        headerPanel.setPreferredSize(new Dimension(900, 70));
        
        JLabel titleLabel = new JLabel("Payment Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel);
        headerPanel.setBorder(BorderFactory.createTitledBorder(
			    BorderFactory.createLineBorder(DARK_RED),  
			    " ",                            
			    TitledBorder.CENTER,          
			    TitledBorder.CENTER,              
			    new Font("Arial", Font.BOLD, 12),  
			    DARK_RED
			));
        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(VERY_LIGHT_RED);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Name field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(DARK_RED);
        
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        centerPanel.add(nameField, gbc);
        
        // Email field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emailLabel.setForeground(DARK_RED);
        
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        centerPanel.add(emailField, gbc);
        
        // Payment method
        JLabel paymentMethodLabel = new JLabel("Payment Method:");
        paymentMethodLabel.setFont(new Font("Arial", Font.BOLD, 16));
        paymentMethodLabel.setForeground(DARK_RED);
        
        JPanel paymentMethodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentMethodPanel.setBackground(VERY_LIGHT_RED);
        
        JRadioButton cashButton = new JRadioButton("Cash");
        cashButton.setFont(new Font("Arial", Font.PLAIN, 16));
        cashButton.setBackground(VERY_LIGHT_RED);
        cashButton.setForeground(DARK_RED);
        cashButton.setSelected(true);
        
        JRadioButton cardButton = new JRadioButton("Card");
        cardButton.setFont(new Font("Arial", Font.PLAIN, 16));
        cardButton.setBackground(VERY_LIGHT_RED);
        cardButton.setForeground(DARK_RED);
        
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cashButton);
        paymentGroup.add(cardButton);
        
        paymentMethodPanel.add(cashButton);
        paymentMethodPanel.add(cardButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(paymentMethodLabel, gbc);
        
        gbc.gridx = 1;
        centerPanel.add(paymentMethodPanel, gbc);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel with buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(LIGHT_RED);
        bottomPanel.setPreferredSize(new Dimension(900, 70));
        
        JButton backButton = createStyledButton("Back", DARK_RED, Color.BLACK);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "ORDER"));
        
        JButton confirmButton = createStyledButton("Confirm Order", DARK_RED, Color.BLACK);
        confirmButton.setBorder(BorderFactory.createCompoundBorder(
	               BorderFactory.createLineBorder(DARK_RED, 3, true),
	               BorderFactory.createEmptyBorder(2, 7, 2, 7)
	           ));        

        confirmButton.addActionListener(e -> {
            customerName = nameField.getText();
            customerEmail = emailField.getText();
            
            if (customerName.isEmpty() || customerEmail.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields", 
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(customerEmail.length() <= 5) {
                JOptionPane.showMessageDialog(frame, "Mail Id Length is insufficient. Please enter valid e-mail ID", 
                        "Missing Information", JOptionPane.WARNING_MESSAGE);            	
                return;            	
            }

            else if((!(customerEmail.lastIndexOf(".") > 2) && 
            		!customerEmail.contains("@"))) {
                JOptionPane.showMessageDialog(frame, "Invalid Mail ID. Please enter valid e-mail ID", 
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;            	
            }
            
            try {
                if (!dataService.checkCustomer(customerEmail)) {
                    dataService.saveCustomerDetails(customerName, customerEmail);
                }

                double billCost = cafeController.getTotalCost(orderingItems);
                logService.log("Bill cost calculated: " + billCost);
                double discountedCost = cafeController.getDiscountedCost(billCost);
                logService.log("Discounted cost calculated: " + discountedCost);
                boolean isOffered = billCost != discountedCost;
 
                cafeController.saveAsActiveOrder(customerEmail, orderingItems, isOffered, discountedCost);
                logService.log("Order saved successfully");

                updateBillScreen();
                logService.log("Bill screen updated successfully");

                cardLayout.show(mainPanel, "BILL");

                orderingItems = null;
                nameField.setText("");
                emailField.setText("");

                logService.log("Order completed by " + customerName + " (" + customerEmail + ") for " + 
                        currencyFormat.format(discountedCost));
            } catch (CustomerFoundException | InvalidMailFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Error processing order: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        bottomPanel.add(backButton);
        bottomPanel.add(Box.createHorizontalStrut(100));
        bottomPanel.setBorder(BorderFactory.createTitledBorder(
			    BorderFactory.createLineBorder(LIGHT_RED),  
			    " ",                            
			    TitledBorder.CENTER,          
			    TitledBorder.CENTER,              
			    new Font("Arial", Font.BOLD, 12),  
			    LIGHT_RED
			));
        bottomPanel.add(confirmButton);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Bill screen
    private JPanel createBillScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(VERY_LIGHT_RED);
        
        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_RED);
        headerPanel.setPreferredSize(new Dimension(900, 70));
        
        JLabel titleLabel = new JLabel("Bill / Receipt");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Center panel with bill details
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(VERY_LIGHT_RED);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(MEDIUM_RED);
        bottomPanel.setPreferredSize(new Dimension(900, 70));
        

        JButton closeButton = createStyledButton("Close", DARK_RED, Color.BLACK);
        closeButton.addActionListener(e -> {
            frame.setVisible(false);        	
            new CafeSimulation(true);
        });

        bottomPanel.add(closeButton);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // update bill screen with current total
    private void updateBillScreen() {
        try {
            JPanel billScreen = (JPanel) mainPanel.getComponent(3);

            JScrollPane scrollPane = null;
            for (Component comp : billScreen.getComponents()) {
                if (comp instanceof JScrollPane) {
                    scrollPane = (JScrollPane) comp;
                    break;
                }
            }
            
            if (scrollPane == null) {
                logService.log("ERROR: Could not find scroll pane in bill screen");
                return;
            }

            JPanel centerPanel = (JPanel) scrollPane.getViewport().getView();

            centerPanel.removeAll();

            JLabel thankYouLabel = new JLabel("Thank you for your order, " + customerName + "!");
            thankYouLabel.setFont(new Font("Arial", Font.BOLD, 18));
            thankYouLabel.setForeground(DARK_RED);
            thankYouLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerPanel.add(thankYouLabel);
            
            centerPanel.add(Box.createVerticalStrut(20));

            JLabel orderDetailsLabel = new JLabel("Order Details:");
            orderDetailsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            orderDetailsLabel.setForeground(DARK_RED);
            orderDetailsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerPanel.add(orderDetailsLabel);
            
            centerPanel.add(Box.createVerticalStrut(10));
   
            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBackground(VERY_LIGHT_RED);
            itemsPanel.setBorder(BorderFactory.createLineBorder(MEDIUM_RED));
            itemsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
   
            for (Map.Entry<String, Integer> entry : orderingItems.entrySet()) {
                String itemId = entry.getKey();
                int quantity = entry.getValue();
                Item item = menuItemsMap.get(itemId);
                
                if (item != null) {
                    JPanel itemRow = new JPanel(new BorderLayout());
                    itemRow.setBackground(VERY_LIGHT_RED);
                    itemRow.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    
                    JLabel itemLabel = new JLabel(quantity + " x " + item.name);
                    JLabel priceLabel = new JLabel(currencyFormat.format(item.cost * quantity));
                    
                    itemRow.add(itemLabel, BorderLayout.WEST);
                    itemRow.add(priceLabel, BorderLayout.EAST);
                    
                    itemsPanel.add(itemRow);
                }
            }
            
            centerPanel.add(itemsPanel);
            centerPanel.add(Box.createVerticalStrut(20));

            double subtotal = 0.0;
            for (Map.Entry<String, Integer> entry : orderingItems.entrySet()) {
                String itemId = entry.getKey();
                int quantity = entry.getValue();
                Item item = menuItemsMap.get(itemId);
                
                if (item != null) {
                    subtotal += item.cost * quantity;
                }
            }

            double discountedTotal = subtotal * 0.9; // 10% discount for testing
            double discount = subtotal - discountedTotal;

            JPanel totalsPanel = new JPanel();
            totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
            totalsPanel.setBackground(LIGHT_RED);
            totalsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            totalsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
  
            JPanel subtotalRow = createBillRow("Subtotal:", currencyFormat.format(subtotal));
            JPanel discountRow = createBillRow("Discount:", currencyFormat.format(discount));
            JPanel totalRow = createBillRow("Total:", currencyFormat.format(discountedTotal));
 
            totalRow.setBackground(MEDIUM_RED);
            totalRow.getComponent(0).setFont(new Font("Arial", Font.BOLD, 16));
            totalRow.getComponent(1).setFont(new Font("Arial", Font.BOLD, 16));
            
            totalsPanel.add(subtotalRow);
            totalsPanel.add(discountRow);
            totalsPanel.add(totalRow);
            
            centerPanel.add(totalsPanel);

            centerPanel.add(Box.createVerticalStrut(20));
            
            JLabel messageLabel = new JLabel("We hope to see you again soon!");
            messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            messageLabel.setForeground(DARK_RED);
            messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            centerPanel.add(messageLabel);

            centerPanel.revalidate();
            centerPanel.repaint();

            logService.log("Bill screen updated successfully");
            
        } catch (Exception e) {
            logService.log("ERROR updating bill screen: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(frame, 
                    "Error generating bill: " + e.getMessage(), 
                    "Bill Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // bill row creator
    private JPanel createBillRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_RED);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(labelComponent, BorderLayout.WEST);
        panel.add(valueComponent, BorderLayout.EAST);
        
        return panel;
    }
    
    // item display populator
    private void populateItemDisplay(List<Item> items) {
        itemDisplayPanel.removeAll();

        logService.log("Displaying " + (items != null ? items.size() : 0) + " items");
        
        if (items == null || items.isEmpty()) {
            JLabel emptyLabel = new JLabel("No items available in this category", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 14));
            emptyLabel.setForeground(DARK_RED);
            itemDisplayPanel.add(emptyLabel, BorderLayout.NORTH);
        } else {
            JPanel gridPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            gridPanel.setBackground(VERY_LIGHT_RED);
            gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));         
            
            for (Item item : items) {
                JPanel itemPanel = createItemPanel(item);
                gridPanel.add(itemPanel);
            }
            
            itemDisplayPanel.add(gridPanel, BorderLayout.NORTH);
        }
        
        itemDisplayPanel.revalidate();
        itemDisplayPanel.repaint();
    }
    
    // item panel creator
    private JPanel createItemPanel(Item item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(MEDIUM_RED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(DARK_RED);
        
        JLabel priceLabel = new JLabel(currencyFormat.format(item.cost));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(ACCENT_RED);
        
        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(priceLabel, BorderLayout.EAST);

        JLabel descriptionLabel = new JLabel("<html><body style='width: 300px'>" + item.description + "</body></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton addButton = createStyledButton("Add to Cart", MEDIUM_RED, Color.BLACK);
        addButton.addActionListener(e -> addItemToCart(item));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(descriptionLabel, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // item to cart adder
    private void addItemToCart(Item item) {
        if (orderingItems.containsKey(item.itemId)) {
            orderingItems.put(item.itemId, orderingItems.get(item.itemId) + 1);
        } else {
            orderingItems.put(item.itemId, 1);
        }

        updateCartPanel();

        logService.log("Added " + item.name + " to cart");
    }
    
    // cart panel with current items
    private void updateCartPanel() {
        JScrollPane scrollPane = (JScrollPane) cartPanel.getComponent(1);
        JPanel cartItemsPanel = (JPanel) scrollPane.getViewport().getView();

        cartItemsPanel.removeAll();

        for (Map.Entry<String, Integer> entry : orderingItems.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            Item item = menuItemsMap.get(itemId);
            
            if (item != null) {
                JPanel itemPanel = new JPanel(new BorderLayout(5, 0));
                itemPanel.setBackground(Color.WHITE);
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, MEDIUM_RED),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));
                
                JLabel quantityLabel = new JLabel(String.valueOf(quantity) + "x");
                quantityLabel.setFont(new Font("Arial", Font.BOLD, 14));
                
                JLabel nameLabel = new JLabel(item.name);
                nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                
                JLabel priceLabel = new JLabel(currencyFormat.format(item.cost * quantity));
                priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                
                JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
                leftPanel.setBackground(Color.WHITE);
                leftPanel.add(quantityLabel);
                leftPanel.add(nameLabel);
                
                JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                rightPanel.setBackground(Color.WHITE);
                
                JButton removeButton = new JButton("X");
                removeButton.setFont(new Font("Arial", Font.BOLD, 10));
                removeButton.setForeground(Color.WHITE);
                removeButton.setBackground(ACCENT_RED);
                removeButton.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                removeButton.setFocusPainted(false);
 
                final String itemIdToRemove = itemId;
                removeButton.addActionListener(e -> {
                    orderingItems.remove(itemIdToRemove);
                    updateCartPanel();
                });
                
                rightPanel.add(priceLabel);
                rightPanel.add(removeButton);
                
                itemPanel.add(leftPanel, BorderLayout.WEST);
                itemPanel.add(rightPanel, BorderLayout.EAST);
                
                cartItemsPanel.add(itemPanel);
            }
        }

        if (orderingItems.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(DARK_RED);
            cartItemsPanel.add(emptyLabel);
        }

        updateCartSummary();

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }
    
    // cart summary with current totals
    private void updateCartSummary() {
        totalCost = 0.0;
        for (Map.Entry<String, Integer> entry : orderingItems.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();
            Item item = menuItemsMap.get(itemId);
            
            if (item != null) {
                totalCost += item.cost * quantity;
            }
        }
        
        discountedCost = cafeController.getDiscountedCost(totalCost);
        double discount = totalCost - discountedCost;

        JPanel summaryPanel = (JPanel) cartPanel.getComponent(2);

        JLabel subtotalLabel = (JLabel) summaryPanel.getComponent(0);
        subtotalLabel.setText("Subtotal: " + currencyFormat.format(totalCost));
        
        JLabel discountLabel = (JLabel) summaryPanel.getComponent(2);
        discountLabel.setText("Discount: " + currencyFormat.format(discount));
        
        JLabel totalLabel = (JLabel) summaryPanel.getComponent(4);
        totalLabel.setText("Total: " + currencyFormat.format(discountedCost));
    }
    
    // divide items into categories
    private List<Item> getItemsByCategory(String category) {
        List<Item> result = new ArrayList<>();
        
        for (Item item : menuItems) {
            if (category.equals("Food") && !(item instanceof Beverage) && !(item instanceof Dessert)) {
                result.add(item);
            } else if (category.equals("Beverage") && item instanceof Beverage) {
                result.add(item);
            } else if (category.equals("Dessert") && item instanceof Dessert) {
                result.add(item);
            }
        }
        
        return result;
    }
    
    // buttons styler
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_RED),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_RED);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    // MAIN
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new FriendlyCafe(null));
    }
}