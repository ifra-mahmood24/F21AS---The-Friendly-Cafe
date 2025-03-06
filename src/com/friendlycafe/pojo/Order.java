/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains about the orders
 */
public class Order {
	public String orderId;
	public String customerId;
	public String timeStamp;
	public HashMap<String, Integer> orderedItems;

	
	/**
	 * @param orderId2
	 * @param string
	 * @param timeStamp
	 * @param orderDetail
	 */
	public Order(String orderId, String customerMailId, String timeStamp, HashMap<String, Integer> orderedItems) {
		// TODO Auto-generated constructor stub
		this.orderId = orderId;
		this.customerId = customerMailId;
		this.timeStamp = timeStamp;
		this.orderedItems = orderedItems;
	}

	/**
	 * 
	 */
	public Order() {
		// TODO Auto-generated constructor stub
	}

	public boolean placeOrder(Map<String, Integer> orderItems) {
		return true;
	}
	
	public Order retriveOrder(String orderId) {
		return null;
	}
	
	public int getOrderCountForaCustomer(String orderId) {
		return 1;
	}
	
}
