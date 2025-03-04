/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 */
public class CustomerContainer {
	@JsonProperty
	List<Customer> customers;

	/**
	 * @return the orders
	 */
	public List<Customer> getCustomers() {
		return customers;
	}
	/**
	 * @param orders the orders to set
	 */
	public void setCustomers(List<Customer> customer) {
		this.customers = customer;
	}

}
