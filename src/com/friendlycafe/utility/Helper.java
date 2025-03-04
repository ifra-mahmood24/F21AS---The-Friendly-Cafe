/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.utility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.CustomerContainer;
import com.friendlycafe.pojo.Order;
import com.friendlycafe.pojo.OrderContainer;
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
		// TODO Auto-generated method stub
        try {
            ObjectMapper objectMapper = new ObjectMapper();
        	helperLogger.info("Writing... "+list);
        	OrderContainer container = new OrderContainer();
        	container.setOrders(list);
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), container);
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
		// TODO Auto-generated method stub
        try {
            ObjectMapper objectMapper = new ObjectMapper();
        	helperLogger.info("Writing... "+list.size());
        	CustomerContainer container = new CustomerContainer();
        	container.setCustomers(list);
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), container);
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
	
	public JSONObject orderToJSON(Order order) {
		JSONObject object = new JSONObject();
		object.put("orderId",order.orderId);
		object.put("customerId",order.customerId);
		object.put("orderedItems",order.orderedItems);
		
		return object;
		
	}
}
