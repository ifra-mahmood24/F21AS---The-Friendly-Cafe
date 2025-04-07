/**]
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.logging.*;

import com.friendlycafe.daoservice.DataAccessService;
import com.friendlycafe.exception.CustomerFoundException;
import com.friendlycafe.exception.InvalidMailFormatException;
import com.friendlycafe.pojo.Beverage;
import com.friendlycafe.pojo.Beverage.DrinkSize;
import com.friendlycafe.pojo.Beverage.TempType;
import com.friendlycafe.pojo.Dessert;
import com.friendlycafe.model.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.pojo.Order;
import com.friendlycafe.pojo.Report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DataService {
	Map<String, Map<String,String>> customers = new HashMap<>();

	private static final Logger logger = Logger.getLogger(DataService.class.getName());

	private DataAccessService daoService = new DataAccessService();
	private CafeService cafeService = new CafeService();

	
	public Order saveOrder(Order order) {
		
		
		logger.info("ON MY WAY TO SAVE THE ORDER!");
		double orderCost = cafeService.calculateCost(order.getOrderedItems());
		order.setCost(orderCost);
		
		ArrayList<Order> allOldOrders = getAllOldOrders();
		allOldOrders.add(order);
		
		//this should go to DAOService
		daoService.writeJSONFileForOrders("src/main/resources/orders.json", allOldOrders);
		logger.info("ORDER SAVED!");
		return order;
	}

	public void saveOrder(String customerMailId, HashMap<String, Integer> orderDetail) {
		Random random = new Random();
		Integer orderId = random.nextInt();
		String timeStamp = LocalDateTime.now().toString();
		Order order = new Order("ORD"+ orderId.toString(), customerMailId, timeStamp, orderDetail);
		ArrayList<Order> allOrders = new ArrayList<>();
		allOrders.add(order);
		

		JSONArray allOrdersAsJSON = daoService.readJSONFile("src/main/resources/orders.json", "orders");
		ObjectMapper objectMapper = new ObjectMapper();

		for(Object oldOrder : allOrdersAsJSON) {
			
			try {
				Order thisOrder = objectMapper.readValue(oldOrder.toString(),Order.class);
				allOrders.add(thisOrder);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		if(!allOrders.isEmpty()) 
			daoService.writeJSONFileForOrders("src/main/resources/orders.json", allOrders);
	}

	public boolean checkCustomer(String mailId) throws CustomerFoundException , InvalidMailFormatException{
		JSONArray customersListAsObject = daoService.readJSONFile("src/main/resources/customers.json", "customers");
		
		for(int index = 0; index < customersListAsObject.length(); index++) {
			JSONObject JsonIndex = customersListAsObject.getJSONObject(index);
			if(JsonIndex.getString("mailId").toString().equalsIgnoreCase(mailId) == true) return true;
		}
		return false;
	}
	
	public boolean saveCustomerDetails(String name, String mailId ) {
		Customer newCustomer = new Customer(name, mailId);
		JSONArray customersListAsObject = daoService.readJSONFile("src/main/resources/customers.json", "customers");
		ArrayList<Customer> allCustomers = new ArrayList<>();

		for(int index = 0; index < customersListAsObject.length(); index++) {
		
			JSONObject JsonIndex = customersListAsObject.getJSONObject(index);
			Customer customer = new Customer(JsonIndex.getString("name"),JsonIndex.getString("mailId"),JsonIndex.getBoolean("isVIP"));
			allCustomers.add(customer);
	
		}
		allCustomers.add(newCustomer);
		
		if(!allCustomers.isEmpty())
			daoService.writeJSONFileForCustomers("src/main/resources/customers.json", allCustomers);

		return false;
		
	}
	
	
	public void generateReport() {
		ArrayList<Report> allOrderedItems  = new ArrayList<>();
		ArrayList<Item> menu = cafeService.getMenu();
		ArrayList<Order> todaysOrders = new ArrayList<>();
		try {	
			JSONArray ordersListAsObject = daoService.readJSONFile("src/main/resources/orders.json", "orders");

			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

			for(int index = 0; index < ordersListAsObject.length(); index++) {

				JSONObject JsonIndex = ordersListAsObject.getJSONObject(index);
				String orderId = JsonIndex.getString("orderId");
				String customerId = JsonIndex.getString("customerId");
				String timeStamp = JsonIndex.getString("timeStamp");
				
				boolean isTodayOrder = LocalDate.parse(timeStamp.split("T")[0], formatter).toString().equals(LocalDate.now().toString());
				
				if(isTodayOrder) {
					JSONObject orderedItemsAsJSON = JsonIndex.getJSONObject("orderedItems");
					Map<String, Object> map = orderedItemsAsJSON.toMap();
					
					HashMap<String, Integer> hashMap = new HashMap<>();
					
					for(Entry<String, Object> entry : map.entrySet()) {
						hashMap.put(entry.getKey(), Integer.parseInt(entry.getValue().toString()));
					}
					Order thisOrder = new Order(orderId, customerId, timeStamp,hashMap);
					
					todaysOrders.add(thisOrder);
				}
					
			}
			logger.info("TODAYS TOTAL ORDER SIZE : "+ todaysOrders.size());
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Integer> map = new HashMap<>();
		Set<String> computedOrders = new HashSet<>();
		
		for(Order order : todaysOrders) 
			for(Entry<String, Integer> orderedItems : order.getOrderedItems().entrySet())
				if(!map.containsKey(orderedItems.getKey()))
					map.put(orderedItems.getKey() , orderedItems.getValue());
				else
					map.put(orderedItems.getKey() ,map.get(orderedItems.getKey()) + orderedItems.getValue());
		
		for(Entry<String, Integer> entry : map.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			for(Item item : menu) 
				if(item.itemId.equals(key)) {
					Report reportOrder = new Report(key,item.name, item.cost, value);
					if(!computedOrders.contains(key)) {
						computedOrders.add(key);
						allOrderedItems.add(reportOrder);
					}
				}
		}
		
		daoService.writeReport(allOrderedItems);

	}
	
	/**
	 * @param order
	 * @return
	 */
	public Order saveAsActiveOrder(Order order) {
		// TODO Auto-generated method stub
		
		logger.info("ON MY WAY TO SAVE THE ACTIVE ORDER!");
		double orderCost = cafeService.calculateCost(order.getOrderedItems());
		order.setCost(orderCost);
		
		ArrayList<Order> allOldOrders = getAllActiveOrders();
		allOldOrders.add(order);
		
		//this should go to DAOService
		saveAsActiveOrders(allOldOrders);
		logger.info("ACTIVE ORDER SAVED!");
		return order;
	}
	
	// remove from activeorder.json and add to order.json
	public void orderServed(Order currentOrder) {
		//saving the order details in orders.json 
		saveOrder(currentOrder);
		
		ArrayList<Order> allActiveOrders = getAllActiveOrders();

		//removing the order data from activeOrders.json
		if(allActiveOrders.contains(currentOrder)) allActiveOrders.remove(currentOrder);
		saveAsActiveOrders(allActiveOrders);
	}
	
	
	
//	-----------------------------INTERNAL HELPER METHOD(CODE READABILITY)-------------------------------------
	

	
	private ArrayList<Order> getAllOldOrders(){
		ArrayList<Order> allOrders = new ArrayList<>();
		JSONArray allOrdersAsJSON = daoService.readJSONFile("src/main/resources/orders.json", "orders");
		ObjectMapper objectMapper = new ObjectMapper();

		for(Object oldOrder : allOrdersAsJSON) {
			
			try {
				Order thisOrder = objectMapper.readValue(oldOrder.toString(),Order.class);
				allOrders.add(thisOrder);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return allOrders;
	}

	
	private ArrayList<Order> getAllActiveOrders(){
		ArrayList<Order> allOrders = new ArrayList<>();
		JSONArray allOrdersAsJSON = daoService.readJSONFile("src/main/resources/activeOrders.json", "orders");
		ObjectMapper objectMapper = new ObjectMapper();

		for(Object oldOrder : allOrdersAsJSON) {
			
			try {
				Order thisOrder = objectMapper.readValue(oldOrder.toString(),Order.class);
				allOrders.add(thisOrder);
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return allOrders;
	}


	/**
	 * @param allActiveOrders
	 */
	private void saveAsActiveOrders(ArrayList<Order> allActiveOrders) {
		// TODO Auto-generated method stub
		daoService.writeJSONFileForOrders("src/main/resources/activeOrders.json", allActiveOrders);

	}

}
