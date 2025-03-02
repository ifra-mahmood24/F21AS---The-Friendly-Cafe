/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.pojo;

/**
 *  This class 
 */
public class Customer {

	public String customerId;
	public String customerName;
	public boolean isVIP;
	public String mailId;
	
	public Customer getCustomerDetails(String customerId) {
		Customer customer = new Customer();
		return customer;
	}
	
	public boolean saveCustomerDetails(String name, String mailId ) {
		return true;
	}

	public boolean updateCustomerDetails(String customerId) {
		return true;
	}
}
