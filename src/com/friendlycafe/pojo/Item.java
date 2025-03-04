/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

/**
 * This class contains details about an item that is in the menu
 */
public class Item {
	public String itemId;
	public String name;
	public String description;
	public float cost;
	
	
	/**
	 * @param itemId
	 * @param name
	 * @param description
	 * @param cost
	 */
	public Item(String itemId, String name, String description, float cost) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.description = description;
		this.cost = cost;
	}

	
	/**
	 * 
	 */
	public Item() {
		// TODO Auto-generated constructor stub
	}


	public boolean addItem(String name, float cost){
		return true;
	}
	
	public boolean updateCost(String itemId, float cost) {
		return true;
	}
	
	public Item getItem(String itemId) {
		// use itemId to find the particular item
		return new Item();
	}
	
	public boolean deleteItem(String itemId) {
		return true;
	}
}
