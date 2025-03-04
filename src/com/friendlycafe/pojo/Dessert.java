package com.friendlycafe.pojo;

public class Dessert extends Item {
     
	public TempType type;
	public DrinkSize size;
	public boolean isRefill;
	
	public enum TempType {
			COLD,
			HOT
		}
	
	public enum DrinkSize {
		 SHORT,
		 TALL,
		 GRANDE
	 }
	 
	/**
	 * @param itemId
	 * @param itemName
	 * @param description
	 * @param cost
	 */
	public Dessert(String itemId, String itemName, String description, Float cost, TempType temp, DrinkSize size) {
		// TODO Auto-generated constructor stub
		this.itemId = itemId;
		this.name = itemName;
		this.description = description;
		this.cost = cost;
		this.size = size;
	}

	public void refill() {}
}
