/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.app;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.service.DataService;
import java.util.*;

/**
 * 
 */
public class FriendlyCafe {

	private static final Logger appLogger = LoggerFactory.getLogger(FriendlyCafe.class);

	public static void main(String[] args) {

		
		appLogger.info(" ----- WELCOME TO THE FRIENDLY CAFE ----- ");

		DataService service = new DataService();
		ArrayList<Item> menu = service.getMenu();
		System.out.println("Item\t\t\t\t\t\tCost");
		for(Item item : menu) {
			System.out.println(item.name +"\t\t\t\t\t£"+item.cost);
		}
		
		// GUI Start
		JLabel welcomeLabel = new JLabel("Welcome to The Friendly Cafe");
		welcomeLabel.setPreferredSize(new Dimension(200,20));
		welcomeLabel.setBounds(1, 20, 80, 25);
		welcomeLabel.setLayout( new GridLayout());
		
		JLabel mailLabel = new JLabel("Mail ID : ");
		mailLabel.setBounds(10, 20, 80, 25);
		JFormattedTextField mailId = new JFormattedTextField();
		mailId.setPreferredSize(new Dimension(200,20));
		mailId.setBounds(100, 20, 180, 25);
		mailId.setLayout( new GridLayout(30, 30) );
		
		JPanel panel = new JPanel();
		panel.setAutoscrolls(true);
		panel.setPreferredSize(new Dimension(100,100));
		panel.add(welcomeLabel);
		panel.add(mailLabel);
		panel.add(mailId);
		String[] menus = new String[menu.size()];
		for(int index = 0; index < menu.size(); index++) 
			menus[index] = menu.get(index).toString(); 
		JList<String> view = new JList<>(menus);
		JScrollPane pane = new JScrollPane(view);
		panel.add(pane);
		
		JFrame frame = new JFrame();
		frame.setTitle("The Friendly cafe");
		frame.add(panel);
		frame.setBounds(50, 50, 50, 50);
		frame.setPreferredSize(new Dimension(500,500));
		frame.pack();
		frame.setVisible(true);
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
