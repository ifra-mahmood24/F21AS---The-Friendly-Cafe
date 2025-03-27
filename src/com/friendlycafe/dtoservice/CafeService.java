package com.friendlycafe.dtoservice;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import com.friendlycafe.pojo.Order;

public class CafeService {
	
	private Logger logger = Logger.getLogger(CafeService.class.getName());
	private DataService dataService  = new DataService();
	
    public HashMap<String, Integer> applyDiscount(HashMap<String, Integer> orderedItems) {
    	
        boolean isFridayFeast = LocalDate.now().getDayOfWeek().toString() == DayOfWeek.FRIDAY.toString();
        boolean isWednesdayBanger = LocalDate.now().getDayOfWeek().toString() == DayOfWeek.WEDNESDAY.toString();
        HashMap<String, Integer> offeredItems = new HashMap<>();
        
        for(Entry<String, Integer> map : orderedItems.entrySet())

        	if(isFridayFeast && map.getValue() > 1)
            	offeredItems.put(map.getKey(), map.getValue() + 1);
            else if( isWednesdayBanger && map.getValue() > 3)
            	offeredItems.put(map.getKey(), map.getValue() + 2);

        return offeredItems;
    }
    
    public void takeOrder(ArrayBlockingQueue<Order> orderQueue) {
		
		// currently keeping 2 servers as constant
		
		final int servers = 2;
		logger.info("--------- TAKING ORDER STARTED ---------"+ orderQueue.size());
		int serve = orderQueue.size() < servers ? orderQueue.size(): servers ;

		for(int i = 0; i < serve; i++) {
			String serverId = "Server_"+(i+1);
			System.out.println("--------- TAKING ORDER FOR -----"+ serve +"--  BY --" + serverId);
			Thread server = new Thread(new TakeOrder(orderQueue), serverId);
			server.start();
			orderQueue.remove();
		}
    }
    
    class TakeOrder implements Runnable {
    	/**
		 * @param orderQueue
		 */
    	private ArrayBlockingQueue<Order> queue;
    	 
		public TakeOrder(ArrayBlockingQueue<Order> orderQueue) {
			// TODO Auto-generated constructor stub
    		queue = orderQueue;
    		logger.info("SIZE OF ORDER QUEUE FOR TAKING ORDER IS : "+queue.size());
		}
    	

		@Override
    	public void run() {
    		// TODO Auto-generated method stub
			try {
	    		logger.info("SIZE OF ORDER QUEUE BEFORE SERVING IS : "+queue.size());
				Order order = queue.take();
	    		processOrder();
	    		dataService.orderServed(order);
	    		logger.info("SIZE OF ORDER QUEUE AFTER SERVING IS : "+queue.size());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		
    	}
    }
    
    
	private void processOrder() {
    	Thread thread = new Thread();
    	try {
    		logger.info("-----STARTING TO PROCESS THE ORDER------");
			thread.sleep(1000);
    		logger.info("-----ORDER PROCESSED------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    	
    }
    

}

