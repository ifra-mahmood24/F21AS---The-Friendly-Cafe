/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.service.DataService;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class FriendlyCafe {

	private static final Logger appLogger = LoggerFactory.getLogger(FriendlyCafe.class);

	public static void main(String[] args) {

		DataService service = new DataService();
		appLogger.info(" Application Started... ");

		//Dummy Data
		Map<String, Map<String,Integer>> order = new HashMap<>(); 

		Map<String, Integer> items = new HashMap<>();

		items.put("Hot Chocolate", 1);
		items.put("Crisps", 2);
		items.put("Brownie",1);
		items.put("Chocolate Croissant", 1);
		order.put("customer0@gmail.com",items);
		
		// Read the data from file
		service.ReadData();

		// Process it
		appLogger.info(" Processing... ");

		// Writ the report and exit the application
		service.generateReport();
	}
}
