package com.friendlycafe.model;

import com.friendlycafe.pojo.Order;


public class Customer {
    private String mailId;
    private String name;
    private Order order;
    
    /**
     * @param customerName
     * @param mailId
     */
    public Customer(String customerName, String mailId, Boolean isVIP) {
        this.name = customerName;
        this.mailId = mailId;
    }
    
    public Customer(String customerName, String mailId) {
        this.name = customerName;
        this.mailId = mailId;
    }

    public Customer() {}
    
    /**
     * Get customer's name
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set customer's name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Get customer's email
     * @return the email
     */
    public String getMailId() {
        return mailId;
    }
    
    /**
     * Set customer's email
     * @param mailId the email to set
     */
    public void setMailId(String mailId) {
        this.mailId = mailId;
    }
    
    
    /**
     * Get customer's order
     * @return the order
     */
    public Order getOrder() {
        return order;
    }
    
    /**
     * Set customer's order
     * @param order the order to set
     */
    public void setOrder(Order order) {
        this.order = order;
    }
    
}