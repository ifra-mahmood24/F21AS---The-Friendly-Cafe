/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.service;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Write logic to read the file and store it
 */
public class DataService {
	Map<String, Map<String,String>> customers = new HashMap<>();

    private static final Logger serviceLogger = LoggerFactory.getLogger(DataService.class);

	// CODE FUNCTIONALLY STARTING AREA
	// logic to read data from file
	// use report folder in /assets/report
	public void ReadData(Map<String, Map<String, Integer>> orderMap) {
		String customerEmail = orderMap.keySet().iterator().next();
		for (Map.Entry<String, Map<String, Integer>> entry : orderMap.entrySet()) {
			serviceLogger.info("entry {}",entry);

		}
		String customer = customerEmail.substring(0,customerEmail.lastIndexOf('@'));
		serviceLogger.info("customer email {}", customer);
		serviceLogger.info("I take care of reading  the data");
	}
	
	public String generateOrderID() {
		return null;
	}
	//EXECUTES WHEN APPLICATION IS GOING TO EXIT
	public void generateReport() {
		
		serviceLogger.info("I take care of generating the report");
	}
}
