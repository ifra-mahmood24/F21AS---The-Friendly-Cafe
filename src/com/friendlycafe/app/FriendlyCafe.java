/**
 * Author 			: prasanths 
 * Last Modified By : prasanths
 */
package com.friendlycafe.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.friendlycafe.service.DataService;

/**
 * 
 */
public class FriendlyCafe {

    private static final Logger appLogger = LoggerFactory.getLogger(FriendlyCafe.class);

	public static void main(String[] args) {
		
		DataService service = new DataService();
		appLogger.info(" Application Started... ");

		// Read the data from file
		service.ReadData();
		
		// Process it
		appLogger.info(" Processing... ");
		
		// Writ the report and exit the application
		service.generateReport();
	}
}
