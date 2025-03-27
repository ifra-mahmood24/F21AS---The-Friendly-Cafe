/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

import com.friendlycafe.dtoservice.DataService;
import com.friendlycafe.pojo.Order;

/**
 * 
 */
public class CafeController {

	private DataService dataService = new DataService();
	
	public Order saveOrder(String customerMailId,  HashMap<String, Integer> orderedItems,
			boolean isOffered, HashMap<String, Integer> offeredItems) {
		Random random = new Random();

		Integer orderId = random.nextInt();
		String timeStamp = LocalDateTime.now().toString();
						
		Order order = new Order("ORD"+ orderId.toString(),
				customerMailId, timeStamp, orderedItems, isOffered, offeredItems);		
		
		return dataService.saveOrder(order);
	}

	/**
	 * @param customerMailId
	 * @param orderedItems
	 * @param isOffered
	 * @param offeredItems
	 */
	public Order saveAsActiveOrder(String customerMailId, HashMap<String, Integer> orderedItems, boolean isOffered,
			HashMap<String, Integer> offeredItems) {
		// TODO Auto-generated method stub
		Random random = new Random();

		Integer orderId = random.nextInt();
		String timeStamp = LocalDateTime.now().toString();
						
		Order order = new Order("ORD"+ orderId.toString(),
				customerMailId, timeStamp, orderedItems, isOffered, offeredItems);		
		
		return dataService.saveAsActiveOrder(order);
		
	}
}
