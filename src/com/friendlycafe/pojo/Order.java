/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

import java.util.HashMap;


/**
 * This class contains about the orders
 */
public class Order {
	private String orderId;
	private String customerId;
	private String timeStamp;
	private HashMap<String, Integer> orderedItems;
	private boolean isDiscounted;
	private HashMap<String, Integer> offeredItems;
	private float cost;


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
	 * @param customerId
	 * @param orderedItems
	 * @param isDiscounted
	 * @param offeredItems
	 */
	public Order(String customerId, HashMap<String, Integer> orderedItems,
			boolean isDiscounted, HashMap<String, Integer> offeredItems) {
		super();
		this.customerId = customerId;
		this.orderedItems = orderedItems;
		this.isDiscounted = isDiscounted;
		this.offeredItems = offeredItems;
	}



	/**
	 * @param orderId
	 * @param customerId
	 * @param timeStamp
	 * @param orderedItems
	 * @param isDiscounted
	 * @param offeredItems
	 */
	public Order(String orderId, String customerId, String timeStamp, HashMap<String, Integer> orderedItems,
			boolean isDiscounted, HashMap<String, Integer> offeredItems) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.timeStamp = timeStamp;
		this.orderedItems = orderedItems;
		this.isDiscounted = isDiscounted;
		this.offeredItems = offeredItems;
	}



	/**
	 * @param orderId
	 * @param customerId
	 * @param timeStamp
	 * @param orderedItems
	 * @param isDiscounted
	 * @param offeredItems
	 * @param cost
	 */
	public Order(String orderId, String customerId, String timeStamp, HashMap<String, Integer> orderedItems,
			boolean isDiscounted, HashMap<String, Integer> offeredItems, float cost) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.timeStamp = timeStamp;
		this.orderedItems = orderedItems;
		this.isDiscounted = isDiscounted;
		this.offeredItems = offeredItems;
		this.cost = cost;
	}



	/**
	 * 
	 */
	public Order() {
		// TODO Auto-generated constructor stub
	}

//	-------GETTERS AND SETTERS START---------
/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the timeStamp
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the orderedItems
	 */
	public HashMap<String, Integer> getOrderedItems() {
		return orderedItems;
	}

	/**
	 * @param orderedItems the orderedItems to set
	 */
	public void setOrderedItems(HashMap<String, Integer> orderedItems) {
		this.orderedItems = orderedItems;
	}

	/**
	 * @return the isDiscounted
	 */
	public boolean isDiscounted() {
		return isDiscounted;
	}

	/**
	 * @param isDiscounted the isDiscounted to set
	 */
	public void setDiscounted(boolean isDiscounted) {
		this.isDiscounted = isDiscounted;
	}

	/**
	 * @return the offeredItems
	 */
	public HashMap<String, Integer> getOfferedItems() {
		return offeredItems;
	}

	/**
	 * @param offeredItems the offeredItems to set
	 */
	public void setOfferedItems(HashMap<String, Integer> offeredItems) {
		this.offeredItems = offeredItems;
	}

	/**
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

//	-------GETTERS AND SETTERS END---------
	
}
