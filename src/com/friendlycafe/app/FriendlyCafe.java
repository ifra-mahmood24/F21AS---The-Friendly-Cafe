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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import java.util.logging.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.pojo.Order;
import com.friendlycafe.controller.CafeController;
import com.friendlycafe.dtoservice.CafeService;
import com.friendlycafe.dtoservice.DataService;
import com.friendlycafe.exception.CustomerFoundException;
import com.friendlycafe.exception.InvalidMailFormatException;
import com.friendlycafe.pojo.Beverage;
import com.friendlycafe.pojo.Dessert;

import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;



/**
 * 
 */
public class FriendlyCafe {

	private static final Logger logger = Logger.getLogger(FriendlyCafe.class.getName());
	

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

		initiateLogger();

		
		logger.info(" ----- WELCOME TO THE FRIENDLY CAFE ----- ");

		//Call services
		DataService dataService = new DataService();
		CafeService cafeService = new CafeService();
		CafeController cafeController = new CafeController();
		ArrayBlockingQueue<Order> orderQueue = new ArrayBlockingQueue<>(100);
		
		//Get Menu from JSON file
		ArrayList<Item> menu = dataService.getMenu();
		

		//Initiate list for total costs display
		ArrayList<Float> orderList = new ArrayList<>();

		//Dividing menu items into its categories
		List<Item> foodItems = new ArrayList<>();
        List<Item> beverageItems = new ArrayList<>();
        List<Item> dessertItems = new ArrayList<>();

		//Save order items and their quantity
		HashMap<String, Integer> orderingItem = new HashMap<>();

		
		// Calling the custom renderer for JList
		ItemListRenderer itemRenderer = new FriendlyCafe().new ItemListRenderer();

		//Sum of orderList will be stored here
		double totalCost[] = new double[1];
		

        for (Item item : menu)         	
            if (item instanceof Beverage) 
                beverageItems.add(item);
            else if (item instanceof Dessert) 
                dessertItems.add(item);
            else 
                foodItems.add(item);

		// --------------------------------------------- GUI Start ---------------------------------------------

		//GUI window
		JFrame frame = new JFrame();
		frame.setTitle("The Friendly cafe");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1000, 1000));

		//Main panel with card layout
		JPanel mainPanel = new JPanel(new CardLayout());
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));

		//Home screen panel card (welcome message, mail ID, and customer name)
		JPanel homePanel = new JPanel();
        homePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel welcomeLabel = new JLabel("Welcome to The Friendly Cafe");
		welcomeLabel.setPreferredSize(new Dimension(400,20));
		JLabel customerNameLabel = new JLabel("Customer Name : ");
		JTextField customerName = new JTextField();
		customerName.setPreferredSize(new Dimension(350,20));
		JLabel mailLabel = new JLabel("Mail ID : ");
		JTextField mailId = new JTextField();
		JLabel processLabel = new JLabel("Your order is in the queue..");
		processLabel.setPreferredSize(new Dimension(400,20));
		mailId.setPreferredSize(new Dimension(380,20));
		homePanel.setAutoscrolls(true);
		homePanel.setPreferredSize(new Dimension(900,900));
		homePanel.setBackground(Color.CYAN);
		homePanel.add(welcomeLabel);
		homePanel.add(customerNameLabel);
		homePanel.add(customerName);
		homePanel.add(mailLabel);
		homePanel.add(mailId);

		//First card of main panel is home panel
		mainPanel.add(homePanel, "HOME");
		
		// Food Panel
        JPanel foodPanel = new JPanel();
		foodPanel.setLayout(new FlowLayout());
        foodPanel.add(new JLabel("Food Items"));
		JList<Item> foodList = new JList<>(foodItems.toArray(new Item[0])); //convert to JList so that it can be shown in JScrollPane
		JLabel totalCostFood = new JLabel();
		foodList.setCellRenderer(itemRenderer); // Set custom renderer for food list
		foodList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)  { // Setting what happens when you select item from list
				int index = foodList.locationToIndex(e.getPoint());
				if (index != -1) {
					
					
					Item selectedItem = foodList.getModel().getElementAt(index);
					orderList.add(selectedItem.cost);
					if(orderingItem.containsKey(selectedItem.itemId))
						orderingItem.put(selectedItem.itemId, orderingItem.get(selectedItem.itemId) + 1); //Increment old quantity value at each click
					else
						orderingItem.put(selectedItem.itemId, 1); // Default quantity value is 1 at first click
					
					totalCost[0] = orderList.stream().mapToDouble(Float::doubleValue).sum(); // Calculate sum of orderList
					totalCostFood.setText(String.format("%.2f",totalCost[0])); // Show current total cost
				}
			
			}
		});
		foodPanel.add(new JScrollPane(foodList));
		foodPanel.add(new JLabel("Current Cost")); // Total cost for dessert panel
		foodPanel.add(totalCostFood);

		//Second card of main panel is food panel
        mainPanel.add(foodPanel, "FOOD");

		// Beverage Panel
        JPanel beveragePanel = new JPanel(new FlowLayout());
        beveragePanel.add(new JLabel("Beverages"));
		JList<Item> beverageList = new JList<>(beverageItems.toArray(new Item[0])); //convert to JList so that it can be shown in JScrollPane
		JLabel totalCostBeverage = new JLabel();
        beverageList.setCellRenderer(itemRenderer);  // Set custom renderer for beverage list
		beverageList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)  { // Setting what happens when you select item from list
				int index = beverageList.locationToIndex(e.getPoint());
				if (index != -1) {
					Item selectedItem = beverageList.getModel().getElementAt(index);
					orderList.add(selectedItem.cost);
					if(orderingItem.containsKey(selectedItem.itemId))
						orderingItem.put(selectedItem.itemId, orderingItem.get(selectedItem.itemId) + 1); //Increment old quantity value at each click
					else
						orderingItem.put(selectedItem.itemId, 1); // Default quantity value is 1 at first click
					totalCost[0] = orderList.stream().mapToDouble(Float::doubleValue).sum(); // Calculate sum of orderList
					logger.info(String.format("%.2f",totalCost[0]));
					totalCostBeverage.setText(String.format("%.2f",totalCost[0])); // Show current total cost
				}
			
			}
		});
        beveragePanel.add(new JScrollPane(beverageList));
		beveragePanel.add(new JLabel("Current Cost")); // Total cost for dessert panel
		beveragePanel.add(totalCostBeverage); 

		//Third card of main panel is beverage panel
        mainPanel.add(beveragePanel, "BEVERAGE");

		// Dessert Panel
        JPanel dessertPanel = new JPanel(new FlowLayout());
        dessertPanel.add(new JLabel("Desserts"));
		JList<Item> dessertList = new JList<>(dessertItems.toArray(new Item[0])); //convert to JList so that it can be shown in JScrollPane
		JLabel totalCostDessert = new JLabel();
        dessertList.setCellRenderer(itemRenderer);  // Set custom renderer for dessert list
        dessertList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)  { // Setting what happens when you select item from list
				int index = dessertList.locationToIndex(e.getPoint());
				if (index != -1) {
					Item selectedItem = dessertList.getModel().getElementAt(index);
					orderList.add(selectedItem.cost); 
					if(orderingItem.containsKey(selectedItem.itemId)) //Increment old quantity value at each click
						orderingItem.put(selectedItem.itemId, orderingItem.get(selectedItem.itemId) + 1);
					else
						orderingItem.put(selectedItem.itemId, 1);// Default quantity value is 1 at first click
					totalCost[0] = orderList.stream().mapToDouble(Float::doubleValue).sum(); // Calculate sum of orderList
					logger.info(String.format("%.2f",totalCost[0]));
					totalCostDessert.setText(String.format("%.2f",totalCost[0])); // Show current total cost
				}
			
			}
		});
        dessertPanel.add(new JScrollPane(dessertList));
		dessertPanel.add(new JLabel("Current Cost")); // Total cost for dessert panel
		dessertPanel.add(totalCostDessert); 

		//Fourth card of main panel is dessert panel
        mainPanel.add(dessertPanel, "DESSERT");


		//Checkout Panel
		JPanel checkoutPanel = new JPanel(new FlowLayout());
		checkoutPanel.add(new JLabel("Checkout"));
		// // Convert ArrayList to an array for JList
        // DefaultListModel<String> listModel = new DefaultListModel<>();
        // for (Float cost : orderList) {
        //     listModel.addElement("£" + cost);  // Format for display
        // }

        // // Create JList and put it inside JScrollPane
        // JList<String> orderJList = new JList<>(listModel);
        // JScrollPane scrollPane = new JScrollPane(orderJList);
		// scrollPane.setBackground(Color.BLUE);
		// scrollPane.setPreferredSize(new Dimension(300,200));
		// // JList<Item> checkoutOrder = new JList<>(orderList.toArray(new Item[0]));
		
		String total = String.format("%.2f",totalCost[0]);
		checkoutPanel.add(new JLabel(total));
		JLabel totalCostLabel = new JLabel("Total Cost");
		
		checkoutPanel.add(totalCostLabel);


		//Fifth card of main panel is checkout panel
		mainPanel.add(checkoutPanel, "CHECKOUT");


		//buttons for item specific pane
		JPanel buttonPanel = new JPanel();
		JButton foodButton = new JButton("Food Items");
		JButton beverageButton = new JButton("Beverage Items");
		JButton dessertButton = new JButton("Dessertt Items");

		//checkout button
		JButton checkoutButton = new JButton("Checkout");

		buttonPanel.add(foodButton);
		buttonPanel.add(beverageButton);
		buttonPanel.add(dessertButton);
		buttonPanel.add(checkoutButton);

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
            cl.show(mainPanel, "DESSERTT");
        });

		checkoutButton.addActionListener(e -> {
			CardLayout cl = (CardLayout) mainPanel.getLayout();
			cl.show(mainPanel, "CHECKOUT");
			try {
			if(!dataService.checkCustomer(mailId.getText()))
				dataService.saveCustomerDetails(customerName.getText().toString(), mailId.getText().toString());
			}catch(CustomerFoundException ex) {
				ex.printStackTrace();
			}catch(InvalidMailFormatException ex) {
				ex.printStackTrace();
			}
			HashMap<String, Integer> offeredItems = cafeService.applyDiscount(orderingItem);
			boolean isOffered = offeredItems.size() > 0;

			Order currentOrder = cafeController.saveAsActiveOrder(mailId.getText(), orderingItem, isOffered, offeredItems);	
			
			// save order in queue
			orderQueue.add(currentOrder);
			System.out.println("ADDED TO ORDERQUEUE ");
			System.out.println("TAKING ORDER");
			cafeService.takeOrder(orderQueue);
			checkoutPanel.add(processLabel);
			checkoutPanel.remove(totalCostLabel);
			
			// Repeat taking the order as much as possible - either until orderQueue is empty or reaching the limit of orderQueue
			//PROCESS OVER IN RELATION TO GUI	

		});

		// Main container to hold panels
        JPanel contentPanel = new JPanel(new BorderLayout());
		// contentPanel.add(totalCostPanel, BorderLayout.SOUTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);		

		
		frame.add(contentPanel);
		frame.setBounds(50, 50, 500, 500);
		frame.setPreferredSize(new Dimension(600,300));
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		// ------------------------------------------------------------ GUI End ------------------------------------------------------------			
		


		// wait for a server to take up the order
		
		
		// process the order...
		

//		 dataService.generateReport();
		
		logger.info("--------------------------------------------- THANKS, VISIT AGAIN --------------------------------------------------");
		
	}



	
	//LOGGING IDEAS - DO THE BELOW IN SEPARATE SINGLETON CLASS
	
	// Create a separate singleton class to initiateLogger
	// use that class's getInstance method to start the initiation and handle the logger

	private static void initiateLogger() {
        
		String logFileName = "app.log";
        File log = new File(logFileName);
        
        if (log.exists())   log.delete();  
        
        try {
		FileHandler fileHandler = new FileHandler(logFileName, true); 
        fileHandler.setFormatter(new SimpleFormatter()); 
        logger.addHandler(fileHandler); 
        fileHandler.flush();
        fileHandler.close();

        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
}
