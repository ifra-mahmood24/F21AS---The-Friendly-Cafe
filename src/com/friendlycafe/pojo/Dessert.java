package com.friendlycafe.pojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.dtoservice.DataService;

public class Dessert extends Item {
     
    private static final Logger dessertLogger = LoggerFactory.getLogger(DataService.class);
    public boolean sugarFree;
	
	
	/**
	 * @param itemId
	 * @param itemName
	 * @param description
	 * @param cost
     * @param sugarFree
	 */
	public Dessert(String itemId, String itemName, String description, Float cost, boolean sugarFree) {
		// TODO Auto-generated constructor stub
		this.itemId = itemId;
		this.name = itemName;
		this.description = description;
		this.cost = cost;
		this.sugarFree = sugarFree;
	}

	public void isSugarFree() {
        if (this.sugarFree == false)
        {
            dessertLogger.info("This item is not sugar-free");
        }
    }
}
