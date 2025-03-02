/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

/**
 * This class extends item class exclusively for beverages
 */
public class Beverage extends Item {
	
	 
	public tempType type;
	public drinkSize size;
	public boolean isRefill;
	
	protected enum tempType {
			COLD,
			HOT
		}
	
	protected enum drinkSize {
		 SHORT,
		 TALL,
		 GRANDE
	 }
	 
	public void refill() {}

}
