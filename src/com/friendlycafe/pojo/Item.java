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
	
	
	public boolean addItem(String name, float cost){
		return true;
	}
	
	public boolean updateCost(String itemId, float cost) {
		return true;
	}
	
	public Item getItem(String itemId) {
		Item item = new Item();
		return item;
	}
	
	public boolean deleteItem(String itemId) {
		return true;
	}
}
