/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.dtoservice.DataService;

/**
 * This class extends item class exclusively for beverages
 */
public class Beverage extends Item {

	private static final Logger beverageLogger = LoggerFactory.getLogger(DataService.class);
	 
	public TempType type;
	public DrinkSize size;
	public Boolean isRefill;
	
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
	public Beverage(String itemId, String itemName, String description, Float cost, TempType temp, DrinkSize size) {
		// TODO Auto-generated constructor stub
		this.itemId = itemId;
		this.name = itemName;
		this.description = description;
		this.cost = cost;
		this.size = size;
	}

	public void refill() {
		if (this.isRefill == true) {
			beverageLogger.info("Refill is true");
		}
	}
}
