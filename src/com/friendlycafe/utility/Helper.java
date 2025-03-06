/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendlycafe.dto.CustomerDTO;
import com.friendlycafe.dto.OrderDTO;
import com.friendlycafe.dto.ReportDTO;
import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.pojo.Order;
import com.friendlycafe.pojo.Report;
import com.friendlycafe.service.DataService;

/**
 * 
 */
public class Helper {
	
	private static final Logger helperLogger = LoggerFactory.getLogger(DataService.class);

	public JSONArray readJSONFile(String Path, String JSONkey){
		
		byte[] bytes;
		try {
			bytes = Files.readAllBytes(Paths.get(Path));
			String content = new String(bytes, StandardCharsets.UTF_8);
			JSONObject object = new JSONObject(content);
			return object.getJSONArray(JSONkey);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	public void writeJSONFileForOrders(String path, ArrayList<Order> list) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
        	helperLogger.info("Writing... "+list);
        	OrderDTO orders = new OrderDTO();
        	orders.setOrders(list);
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), orders);
		} catch (StreamWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeJSONFileForCustomers(String path, ArrayList<Customer> list) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
        	helperLogger.info("Writing... "+list.size());
        	CustomerDTO customers = new CustomerDTO();
        	customers.setCustomers(list);
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), customers);
		} catch (StreamWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void writeReport(ArrayList<Report> allOrderedItems) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		LocalDate date = LocalDate.now();
		Double earningForTheDay = 0.0;
		for(Report itemTotalCost: allOrderedItems) {
			earningForTheDay += itemTotalCost.totalCost;
		}
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/report_"+date+".json"),
					new ReportDTO(allOrderedItems, earningForTheDay));
		} catch (Exception e) {
			helperLogger.info("WRITE REPORT FAILING....");
			e.printStackTrace();
		}		
	}
	
	
	public JSONObject orderToJSON(Order order) {
		JSONObject object = new JSONObject();
		object.put("orderId",order.orderId);
		object.put("customerId",order.customerId);
		object.put("orderedItems",order.orderedItems);
		
		return object;
		
	}
}
