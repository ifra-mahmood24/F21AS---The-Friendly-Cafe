package com.friendlycafe.dtoservice;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

public class CafeService {
    public HashMap<String, Integer> applyDiscount(HashMap<String, Integer> orderedItems) {
        boolean isFridayFeast = LocalDate.now().getDayOfWeek().toString() == DayOfWeek.FRIDAY.toString();
        boolean isWednesdayBanger = LocalDate.now().getDayOfWeek().toString() == DayOfWeek.WEDNESDAY.toString();
        HashMap<String, Integer> offeredItems = new HashMap<>();
        for(Entry<String, Integer> map : orderedItems.entrySet()){
            if(isFridayFeast && map.getValue() > 1)
            	offeredItems.put(map.getKey(), map.getValue() + 1);
            if( isWednesdayBanger && map.getValue() > 3)
            	offeredItems.put(map.getKey(), map.getValue() + 2);
        }

        return offeredItems;
    }
    // public static void saveOrder(String customerId, String timeStamp, Map<String, Integer> orderedItems, double finalCost) {
    //     try {
    //         JSONObject order = new JSONObject();
    //         order.put("customerId", customerId);
    //         order.put("timeStamp", timeStamp);
    //         order.put("orderedItems", orderedItems);
    //         order.put("finalCost", finalCost);
            
    //         Path filePath = Path.of("activeOrders.json");
    //         JSONArray orders;
            
    //         if (Files.exists(filePath)) {
    //             String content = Files.readString(filePath);
    //             orders = new JSONArray(content);
    //         } else {
    //             orders = new JSONArray();
    //         }
            
    //         orders.put(order);
    //         Files.writeString(filePath, orders.toString(4)); // Save with indentation
    //     } catch (Exception e) {
    //         System.out.println("Error saving order: " + e.getMessage());
    //     }
    // }
    
    // public static void main(String[] args) {
        
    //     HashMap<String, Integer> orderedItems = new HashMap<>();
    //     orderedItems.put("B501", 4);
    //     orderedItems.put("F001", 4);
        
    //     String customerId = "C123"; // Example customer ID
    //     String timeStamp = LocalDateTime.now().toString(); // Example timestamp
        
    //     HashMap<String, Integer> finalCost = applyDiscount(orderedItems);
    //     System.out.println("Final Order Cost: " + finalCost);
        
    //     // saveOrder(customerId, timeStamp, orderedItems, finalCost);
    // }
}

