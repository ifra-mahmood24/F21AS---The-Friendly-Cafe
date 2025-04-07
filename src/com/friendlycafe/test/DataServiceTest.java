package com.friendlycafe.test;
import org.junit.jupiter.api.*;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

import com.friendlycafe.controller.CafeController;
import com.friendlycafe.daoservice.DataAccessService;
import com.friendlycafe.service.DataService;
import com.friendlycafe.exception.CustomerFoundException;
import com.friendlycafe.exception.InvalidMailFormatException;
import com.friendlycafe.pojo.Item;


public class DataServiceTest {
    private DataService dataService = new DataService();
    private CafeController cafeController = new CafeController();
    private DataAccessService daoService = new DataAccessService();


    @Test
    void testGetMenu_Success() {
        // Mock JSON data for food, beverage, and dessert items
        JSONArray mockFoodItems = new JSONArray();
        JSONObject foodItem = new JSONObject();
        foodItem.put("itemId", "F001");
        foodItem.put("name", "Burger");
        foodItem.put("description", "Double patty cheese burger");
        foodItem.put("cost", "5.99");
        mockFoodItems.put(foodItem);

        // Simulate the JSON response
        when(daoService.readJSONFile(anyString(), eq("foodItems"))).thenReturn(mockFoodItems);
        when(daoService.readJSONFile(anyString(), eq("beverageItems"))).thenReturn(new JSONArray());
        when(daoService.readJSONFile(anyString(), eq("dessertItems"))).thenReturn(new JSONArray());
        
        ArrayList<Item> menu = cafeController.getMenu();
        assertNotNull(menu, "Menu should not be null");
        assertEquals(1, menu.size(), "Menu should contain 1 item");
        assertEquals("Burger", menu.get(0).name, "First item should be Burger");
    }

    
    @Test
    void testGetMenu_EmptyFile() {
        Mockito.when(daoService.readJSONFile(Mockito.anyString(), Mockito.anyString())).thenReturn(new JSONArray());

        List<Item> menu = cafeController.getMenu();
        assertNotNull(menu, "Menu should not be null");
        assertEquals(0, menu.size(), "Menu should be empty");
    }
    
    @Test
    void testSaveOrder_ValidOrder() {
        HashMap<String, Integer> orderDetails = new HashMap<>();
        orderDetails.put("F001", 2);
        orderDetails.put("B002",1);
        
        assertDoesNotThrow(() -> dataService.saveOrder("test@example.com", orderDetails), "Saving order should not throw an exception");
    }

    @Test
    void testSaveOrder_EmptyOrder() {
        HashMap<String, Integer> emptyOrder = new HashMap<>();

        assertDoesNotThrow(() -> dataService.saveOrder("test@example.com", emptyOrder), "Saving an empty order should not throw an exception");
    }
    
    @Test
    void testCheckCustomer_ExisingCustomer() throws CustomerFoundException, InvalidMailFormatException {
        JSONArray mockCustomers = new JSONArray();
        JSONObject customer = new JSONObject();
        customer.put("mailId", "test@gmail.com");
        mockCustomers.put(customer);
        when(daoService.readJSONFile(anyString(), eq("customers"))).thenReturn(mockCustomers);
        
        assertTrue(dataService.checkCustomer("test@gmail.com"), "Existing customer should return true");
    }
    
    @Test
    void testCheckCustomer_NonExistent() throws CustomerFoundException, InvalidMailFormatException {
        JSONArray emptyCustomersList = new JSONArray();
        Mockito.when(daoService.readJSONFile(Mockito.anyString(), Mockito.eq("customers"))).thenReturn(emptyCustomersList);

        assertFalse(dataService.checkCustomer("unknown@example.com"), "Non-existing customer should return false");
    }
    
    @Test
    void testGenerateReport_NoOrders() {
        JSONArray emptyOrdersList = new JSONArray();
        Mockito.when(daoService.readJSONFile(Mockito.anyString(), Mockito.eq("orders"))).thenReturn(emptyOrdersList);

        assertDoesNotThrow(() -> dataService.generateReport(), "Generating a report with no orders should not throw an exception");
    }
    
    @Test
    void testGenerateReport_ValidOrders() {
        JSONArray ordersList = new JSONArray();
        JSONObject order = new JSONObject();
        order.put("orderId", "ORD123");
        order.put("customerId", "test@example.com");
        order.put("timeStamp", "2025-03-06T12:00:00");
        JSONObject items = new JSONObject();
        items.put("F001", 2);
        order.put("orderedItems", items);
        ordersList.put(order);

        Mockito.when(daoService.readJSONFile(Mockito.anyString(), Mockito.eq("orders"))).thenReturn(ordersList);

        assertDoesNotThrow(() -> dataService.generateReport(), "Generating a report with valid orders should not throw an exception");
    }
}
