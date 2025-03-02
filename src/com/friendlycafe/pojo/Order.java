/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

import java.util.Map;

/**
 * This class contains about the orders
 */
public class Order {
	public String orderId;
	public String customerId;
	public Map<Item, Integer> orderedItems;
	
	public boolean placeOrder(Map<Item, Integer> orderItems) {
		return true;
	}
	
	public Order retriveOrder(String orderId) {
		Order order = new Order();
		return order;
	}
	
	public int getOrderCountForaCustomer(String orderId) {
		return 1;
	}
	
}
