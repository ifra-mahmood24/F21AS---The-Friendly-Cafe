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
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendlycafe.dto.CustomerDTO;
import com.friendlycafe.dto.OrderDTO;
import com.friendlycafe.dto.ReportDTO;
import com.friendlycafe.dtoservice.DataService;
import com.friendlycafe.pojo.Customer;
import com.friendlycafe.pojo.Item;
import com.friendlycafe.pojo.Order;
import com.friendlycafe.pojo.Report;

/**
 * 
 */
public class Helper {	
	
//	public JSONObject orderToJSON(Order order) {
//		JSONObject object = new JSONObject();
//		object.put("orderId",order.getOrderId());
//		object.put("customerId",order.getCustomerId());
//		object.put("orderedItems",order.getOrderedItems());
//		
//		return object;
//		
//	}
	

}
