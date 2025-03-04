/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.app;


import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.service.DataService;
import com.friendlycafe.exceptions.*;

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

		
		// GET THE BELOW TWO VALUES FROM GUI
		HashMap<String, Integer> orderingItem = new HashMap<>();
		orderingItem.put(menu.get(0).itemId, 4);
		orderingItem.put(menu.get(2).itemId, 4);
		
		Customer customer = new Customer("aaa", "dsa@gmail.com");
		// GET THE ABOVE customer and orderingItems TWO VALUES FROM GUI
		
		try{
			if(!service.checkCustomer(customer.mailId))
				throw new CustomerFoundException("Customer with Mail ID: " + customer.mailId + " already exists");
			else {
				service.saveCustomerDetails("aaa", "aaa@aaa.com");
				appLogger.info("Customer Details Saved");
			}
		}
		catch (CustomerFoundException e){
			appLogger.error("Exception came : "+e.getMessage());
			}
		service.saveOrder(customer.mailId, orderingItem);
	
		
//		service.generateReport();
	}
}
