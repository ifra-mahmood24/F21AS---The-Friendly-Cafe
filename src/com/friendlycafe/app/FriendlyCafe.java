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
import javax.swing.table.DefaultTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.pojo.Item;
import com.friendlycafe.service.DataService;

/**
 * 
 */
public class FriendlyCafe {

    private static final Logger appLogger = LoggerFactory.getLogger(FriendlyCafe.class);

	public static void main(String[] args) {
		
		appLogger.info(" Application Started... ");

		DataService service = new DataService();
		ArrayList<Item> menu = service.getMenu();
		
		for(Item item : menu) {
			appLogger.info("Item Name : "+item.name);
		}
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
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
//		panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		panel.setAutoscrolls(true);
		panel.setPreferredSize(new Dimension(100,100));
		panel.add(welcomeLabel);
		panel.add(mailLabel);
		panel.add(mailId);
		String[] menus = new String[menu.size()];
		for(int index = 0; index < menu.size(); index++) {
			menus[index] = menu.get(index).toString(); 
		}
		JList<String> view = new JList<>(menus);
		JScrollPane pane = new JScrollPane(view);
		panel.add(pane);
		String[] columns = new String[] {"Item Name", "Description", "Price"};
		
		DefaultTableModel tableModel = new DefaultTableModel();
		
		frame.setTitle("The Friendly cafe");
		frame.add(panel);
		frame.setBounds(50, 50, 50, 50);
		frame.setPreferredSize(new Dimension(500,500));
		frame.pack();
		frame.setVisible(true);

/*			
		// GET THE BELOW TWO VALUES FROM GUI
		
		HashMap<String, Integer> orderingItem = new HashMap<>();
	
		orderingItem.put(menu.get(0).itemId, 4);
		orderingItem.put(menu.get(2).itemId, 4);
		
		
		Customer customer = new Customer("aaa", "dsa@gmail.com");
		
		if(!service.checkCustomer(customer.mailId))
			service.saveCustomerDetails("aaa", "aaa@aaa.com");
		
		service.saveOrder(customer.mailId, orderingItem);
*/	
		
//		service.generateReport();
	}
}
