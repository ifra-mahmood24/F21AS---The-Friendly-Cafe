/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.charset.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.pojo.Beverage;
import com.friendlycafe.pojo.Beverage.DrinkSize;
import com.friendlycafe.pojo.Beverage.TempType;
import com.friendlycafe.pojo.Dessert;
import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.pojo.Order;
import com.friendlycafe.utility.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Write logic to read the file and store it
 */
public class DataService {
	Map<String, Map<String,String>> customers = new HashMap<>();

	private static final Logger serviceLogger = LoggerFactory.getLogger(DataService.class);

	public static final ArrayList<Item> menuList = new ArrayList<>();
	
	// CODE FUNCTIONALLY STARTING AREA
	// logic to read data from file
	// use report folder in /assets/report
	// public void ReadData(Map<String, Map<String, Integer>> orderMap) {
	// 	String customerEmail = orderMap.keySet().iterator().next();
	// 	for (Map.Entry<String, Map<String, Integer>> entry : orderMap.entrySet()) {
	// 		serviceLogger.info("entry {}",entry);

	// 	}
	// 	String customer = customerEmail.substring(0,customerEmail.lastIndexOf('@'));
	// 	serviceLogger.info("customer email {}", customer);
	// 	serviceLogger.info("I take care of reading  the data");
	// }
	
	// public String generateOrderID() {
	// 	return null;
	// }
	//EXECUTES WHEN APPLICATION IS GOING TO EXIT
	public ArrayList<Item> getMenu() {
		try {
			Helper utility = new Helper();

			JSONArray foodItemListAsObject = utility.readJSONFile("src/main/resources/foodMenu.json", "foodItems");
			JSONArray beverageItemListAsObject = utility.readJSONFile("src/main/resources/beverageMenu.json", "beverageItems");
			JSONArray dessertItemListAsObject = utility.readJSONFile("src/main/resources/dessertMenu.json", "dessertItems");

			ArrayList<Item> foodItemList = new ArrayList<>();
			ArrayList<Item> beverageItemList = new ArrayList<>();
			ArrayList<Item> dessertItemList = new ArrayList<>();

			
			//Reading Food Menu List 
			for(int index = 0; index < foodItemListAsObject.length(); index++) {
				
				JSONObject JsonIndex = foodItemListAsObject.getJSONObject(index);
				
				String itemId = JsonIndex.getString("itemId");
				String itemName = JsonIndex.getString("name");
				String description = JsonIndex.getString("description");
				Float cost = Float.parseFloat(JsonIndex.getString("cost"));
				
				Item foodItem = new Item(itemId, itemName, description, cost);
				foodItemList.add(foodItem);
			}
			
			//Reading Beverage Menu List 
			for(int index = 0; index < beverageItemListAsObject.length(); index++) {
				
				JSONObject JsonIndex = beverageItemListAsObject.getJSONObject(index);
				
				String itemId = JsonIndex.getString("itemId");
				String itemName = JsonIndex.getString("name");
				String description = JsonIndex.getString("description");
				Float cost = Float.parseFloat(JsonIndex.getString("cost"));
				String temp = JsonIndex.getString("temp");
				String size = JsonIndex.getString("size");
				//Possible exception for Temp or size value not matching with enum type
				Beverage beverage = new Beverage(itemId, itemName, description, cost, TempType.valueOf(temp), DrinkSize.valueOf(size));
				
				beverageItemList.add(beverage);
			}

			//Reading Dessert List
			for (int index = 0; index < dessertItemListAsObject.length(); index++) {

				JSONObject JsonIndex = dessertItemListAsObject.getJSONObject(index);

				String itemId = JsonIndex.getString("itemId");
				String itemName = JsonIndex.getString("name");
				String description = JsonIndex.getString("description");
				Float cost = Float.parseFloat(JsonIndex.getString("cost"));
				Boolean sugarFree = JsonIndex.getBoolean("sugarFree");

				Dessert dessert = new Dessert(itemId, itemName, description, cost, sugarFree);
				dessertItemList.add(dessert);
			}

			menuList.addAll(foodItemList);
			menuList.addAll(beverageItemList);
			menuList.addAll(dessertItemList);
			
//			serviceLogger.info("Data Read & wrote in memory");
			return menuList;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// EXECUTES WHEN APPLICATION IS GOING TO EXIT
	public void generateReport() {

//		serviceLogger.info("I take care of generating the report");
	}

	/**
	 * 
	 */
	public void saveOrder(String customerMailId, HashMap<String, Integer> orderDetail) {
		// TODO Auto-generated method stub
		Random random = new Random();
		Integer orderId = random.nextInt();
		Helper utility = new Helper();
		String timeStamp = LocalDateTime.now().toString();
		Order order = new Order("ORD"+ orderId.toString(), customerMailId, timeStamp, orderDetail);
		ArrayList<Order> allOrders = new ArrayList<>();
		allOrders.add(order);
		
//		serviceLogger.info(" OrderList size : "+allOrders.size());

		JSONArray allOrdersAsJSON = utility.readJSONFile("src/main/resources/orders.json", "orders");
		ObjectMapper objectMapper = new ObjectMapper();

		for(Object oldOrder : allOrdersAsJSON) {
//			serviceLogger.info("OLD ORDERS : "+oldOrder);
			
			try {
				Order thisOrder = objectMapper.readValue(oldOrder.toString(),Order.class);
				allOrders.add(thisOrder);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		serviceLogger.info(" Order as list : "+allOrders.toString());
		if(!allOrders.isEmpty()) {
			utility.writeJSONFileForOrders("src/main/resources/orders.json", allOrders);
		}		
	}

	public boolean checkCustomer(String mailId) {
		Helper utility = new Helper();
		JSONArray customersListAsObject = utility.readJSONFile("src/main/resources/customers.json", "customers");
		for(int index = 0; index < customersListAsObject.length(); index++) {
			JSONObject JsonIndex = customersListAsObject.getJSONObject(index);
			serviceLogger.info( mailId + " ==?  "+JsonIndex.getString("mailId").toString()+" ");
			if(JsonIndex.getString("mailId").toString().equalsIgnoreCase(mailId) == true) return true;
		}
		return false;
	}
	
	public boolean saveCustomerDetails(String name, String mailId ) {
		Helper utility = new Helper();
		Customer newCustomer = new Customer(name, mailId);
		JSONArray customersListAsObject = utility.readJSONFile("src/main/resources/customers.json", "customers");

		ArrayList<Customer> allCustomers = new ArrayList<>();
		for(int index = 0; index < customersListAsObject.length(); index++) {
			JSONObject JsonIndex = customersListAsObject.getJSONObject(index);
			Customer customer = new Customer(JsonIndex.getString("name"),JsonIndex.getString("mailId"),JsonIndex.getBoolean("isVIP"));
			allCustomers.add(customer);
			
//			serviceLogger.info(" OrderList size : "+allOrders.size());
	
		}
		allCustomers.add(newCustomer);
		if(!allCustomers.isEmpty()) {
			utility.writeJSONFileForCustomers("src/main/resources/customers.json", allCustomers);
		}
		return false;
		
	}
}
