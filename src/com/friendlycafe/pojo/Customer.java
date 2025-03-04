/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

/**
 *  This class 
 */
public class Customer {

	public String mailId;
	public String name;
	public boolean isVIP = false;
	
	/**
	 * @param customerName
	 * @param mailId
	 */
	public Customer(String customerName, String mailId, Boolean isVIP) {
		this.name = customerName;
		this.mailId = mailId;
		this.isVIP = isVIP;
	}
	public Customer(String customerName, String mailId) {
		this.name = customerName;
		this.mailId = mailId;
	}

	public Customer() {}
	
	public Customer getCustomerDetails(String mailId) {
		Customer customer = new Customer();
		return customer;
	}
	
	public boolean saveCustomerDetails(String name, String mailId ) {
		this.name = name;
		this.mailId = mailId;
		return true;
	}

	public boolean updateCustomerDetails(String customerId) {
		return true;
	}
}
