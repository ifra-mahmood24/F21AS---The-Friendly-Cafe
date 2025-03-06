/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.pojo.Beverage;
import com.friendlycafe.pojo.Dessert;
import com.friendlycafe.service.DataService;
import com.friendlycafe.service.CafeService;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;



/**
 * 
 */
public class FriendlyCafe {

	private static final Logger appLogger = LoggerFactory.getLogger(FriendlyCafe.class);

	// Custom cell renderer to display item name and cost in JList
	class ItemListRenderer extends JLabel implements ListCellRenderer<Item> {
		@Override
		public Component getListCellRendererComponent(JList<? extends Item> list, Item value, int index,
				boolean isSelected, boolean cellHasFocus) {
			setText(value.name + " - £" + value.cost); // Display name and cost
			setOpaque(true);
			if (isSelected) {
				setBackground(Color.LIGHT_GRAY); // Highlight selected item
			} else {
				setBackground(Color.WHITE); // Default background
			}
			return this;
		}
	}
	public static void main(String[] args) {

		
		appLogger.info(" ----- WELCOME TO THE FRIENDLY CAFE ----- ");

		//Call services
		DataService service = new DataService();
		CafeService service2 = new CafeService();

		//Get Menu from JSON file
		ArrayList<Item> menu = service.getMenu();

		// //Printing all items in menu
		// System.out.println("Item\t\t\t\t\t\tCost");
		// for(Item item : menu) {
		// 	System.out.println(item.name +"\t\t\t\t\t£"+item.cost);
		// }
		
		// Calling the custom renderer for JList
		ItemListRenderer itemRenderer = new FriendlyCafe().new ItemListRenderer();

		//Initiate list for total costs display
		ArrayList<Float> orderList = new ArrayList<>();
		//Sum of orderList will be stored here
		double totalCost[];
		totalCost = new double[1];

		service2.applyDiscount(totalCost[0]);

		//Dividing menu items into its categories
		List<Item> foodItems = new ArrayList<>();
        List<Item> beverageItems = new ArrayList<>();
        List<Item> dessertItems = new ArrayList<>();

        for (Item item : menu) {
            if (item instanceof Beverage) {
                beverageItems.add(item);
            } else if (item instanceof Dessert) {
                dessertItems.add(item);
            } else {
                foodItems.add(item);
            }
        }


		// GUI Start

		//GUI window
		JFrame frame = new JFrame();
		frame.setTitle("The Friendly cafe");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 500));

		//Main panel with card layout
		JPanel mainPanel = new JPanel(new CardLayout());
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));

		//Home screen panel card (welcome message, mail ID, and customer name)
		JPanel homePanel = new JPanel();
        homePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel welcomeLabel = new JLabel("Welcome to The Friendly Cafe");
		welcomeLabel.setPreferredSize(new Dimension(400,20));
		JLabel customerNameLabel = new JLabel("Customer Name : ");
		JFormattedTextField customerName = new JFormattedTextField();
		customerName.setPreferredSize(new Dimension(400,20));
		JLabel mailLabel = new JLabel("Mail ID : ");
		JFormattedTextField mailId = new JFormattedTextField();
		mailId.setPreferredSize(new Dimension(200,20));
		homePanel.setAutoscrolls(true);
		homePanel.setPreferredSize(new Dimension(500,500));
		homePanel.setBackground(Color.CYAN);
		homePanel.add(welcomeLabel);
		homePanel.add(customerNameLabel);
		homePanel.add(mailLabel);
		homePanel.add(mailId);

		//First card of main panel is home panel
		mainPanel.add(homePanel, "HOME");
		
		// Food Panel
        JPanel foodPanel = new JPanel();
		foodPanel.setLayout(new FlowLayout());
        foodPanel.add(new JLabel("Food Items"));
		JList<Item> foodList = new JList<>(foodItems.toArray(new Item[0]));
		JLabel totalCostFood = new JLabel();
		foodList.setCellRenderer(itemRenderer); // Set custom renderer for food list
		foodList.addListSelectionListener(e -> { // Setting what happens when you select item from list
			if (!e.getValueIsAdjusting()) {
				Item selectedItem = foodList.getSelectedValue();
				if (selectedItem != null) {
					orderList.add(selectedItem.cost); // Add selected item to orderList
					appLogger.info("{}", orderList);
					appLogger.info("{}", orderList.stream().mapToDouble(Float::doubleValue).sum());
					totalCost[0] = orderList.stream().mapToDouble(Float::doubleValue).sum(); // Calculate sum of orderList
					appLogger.info(String.format("%.2f",totalCost[0]));
					totalCostFood.setText(String.format("%.2f",totalCost[0])); // Show current total cost
				}
			}
		});
		foodPanel.add(new JScrollPane(foodList));
		foodPanel.add(totalCostFood); // Total cost for food panel

		//Second card of main panel is food panel
        mainPanel.add(foodPanel, "FOOD");

		// Beverage Panel
        JPanel beveragePanel = new JPanel(new FlowLayout());
        beveragePanel.add(new JLabel("Beverages"));
		JList<Item> beverageList = new JList<>(beverageItems.toArray(new Item[0]));
		JLabel totalCostBeverage = new JLabel();
        beverageList.setCellRenderer(itemRenderer);  // Set custom renderer for beverage list
        beverageList.addListSelectionListener(e -> { // Setting what happens when you select item from list
            if (!e.getValueIsAdjusting()) {
                Item selectedItem = beverageList.getSelectedValue();
                if (selectedItem != null) {
					orderList.add(selectedItem.cost);  // Add selected item to orderList
					appLogger.info("{}", orderList);
					appLogger.info("{}", orderList.stream().mapToDouble(Float::doubleValue).sum());
					totalCost[0] = orderList.stream().mapToDouble(Float::doubleValue).sum(); // Calculate sum of orderList
					appLogger.info(String.format("%.2f",totalCost[0]));
					totalCostBeverage.setText(String.format("%.2f",totalCost[0])); // Show current total cost
				}
            }
        });
        beveragePanel.add(new JScrollPane(beverageList));
		beveragePanel.add(totalCostBeverage); // Total cost for beverage panel

		//Third card of main panel is beverage panel
        mainPanel.add(beveragePanel, "BEVERAGE");

		// Dessert Panel
        JPanel dessertPanel = new JPanel(new FlowLayout());
        dessertPanel.add(new JLabel("Desserts"));
		JList<Item> dessertList = new JList<>(dessertItems.toArray(new Item[0]));
		JLabel totalCostDessert = new JLabel();
        dessertList.setCellRenderer(itemRenderer);  // Set custom renderer for dessert list
        dessertList.addListSelectionListener(e -> { // Setting what happens when you select item from list
            if (!e.getValueIsAdjusting()) {
                Item selectedItem = dessertList.getSelectedValue();
                if (selectedItem != null) {
					orderList.add(selectedItem.cost);  // Add selected item to orderList
					appLogger.info("{}", orderList);
					appLogger.info("{}", orderList.stream().mapToDouble(Float::doubleValue).sum());
					totalCost[0] = orderList.stream().mapToDouble(Float::doubleValue).sum(); // Calculate sum of orderList
					appLogger.info(String.format("%.2f",totalCost[0]));
					totalCostDessert.setText(String.format("%.2f",totalCost[0])); // Show current total cost
                }
            }
        });
        dessertPanel.add(new JScrollPane(dessertList));
		dessertPanel.add(totalCostDessert); // Total cost for dessert panel

		//Fourth card of main panel is dessert panel
        mainPanel.add(dessertPanel, "DESSERT");

		//buttons for item specific pane
		JPanel buttonPanel = new JPanel();
		JButton foodButton = new JButton("Food Items");
		JButton beverageButton = new JButton("Beverage Items");
		JButton dessertButton = new JButton("Dessert Items");

		buttonPanel.add(foodButton);
		buttonPanel.add(beverageButton);
		buttonPanel.add(dessertButton);

		// Add button functionalities
        foodButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "FOOD");
        });

        beverageButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "BEVERAGE");
        });

        dessertButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "DESSERT");
        });

		// Main container to hold panels
        JPanel contentPanel = new JPanel(new BorderLayout());
		// contentPanel.add(totalCostPanel, BorderLayout.SOUTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);		

		
		frame.add(contentPanel);
		frame.setBounds(50, 50, 500, 500);
		frame.setPreferredSize(new Dimension(450,300));
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		// GUI End

		
		// GET VALUES FROM GUI(Customer Name, Email, order)
		Customer customer = new Customer("aaa", "dsa@mail.com");
		HashMap<String, Integer> orderingItem = new HashMap<>();	

		//compiling the items for an order
		orderingItem.put(menu.get(1).itemId, 1);
		orderingItem.put(menu.get(2).itemId, 2);		
		orderingItem.put(menu.get(3).itemId, 1);		
		orderingItem.put(menu.get(4).itemId, 2);		
		
		
		
		//Checking whether the customer is already existing in our records
		if(!service.checkCustomer(customer.mailId))
			service.saveCustomerDetails("teat", "test@gmail.com");
		
		service.saveOrder(customer.mailId, orderingItem);

		
		service.generateReport();
		
		appLogger.info("----- THANKS, VISIT AGAIN -----");
	}
}
