package com.friendlycafe.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.friendlycafe.pojo.Item;
import com.friendlycafe.utility.Helper;

@ExtendWith(MockitoExtension.class)
public class DataServiceTest {
    
    @InjectMocks
    private DataService dataService;
    
    @Mock
    private Helper utility;
    
    @BeforeEach
    void setUp() {
        dataService = new DataService();
    }
    
    @Test
    void testGetMenu() {
        JSONArray mockFoodItems = new JSONArray();
        JSONObject foodItem = new JSONObject();
        foodItem.put("itemId", "F001");
        foodItem.put("name", "Burger");
        foodItem.put("description", "Double patty cheese burger");
        foodItem.put("cost", "5.99");
        mockFoodItems.put(foodItem);
        
        when(utility.readJSONFile(anyString(), eq("foodItems"))).thenReturn(mockFoodItems);
        when(utility.readJSONFile(anyString(), eq("beverageItems"))).thenReturn(new JSONArray());
        when(utility.readJSONFile(anyString(), eq("dessertItems"))).thenReturn(new JSONArray());
        
        ArrayList<Item> menu = dataService.getMenu();
        assertNotNull(menu);
        assertEquals(1, menu.size());
        assertEquals("Burger", menu.get(0).getName());
    }
    
    @Test
    void testSaveOrder() {
        HashMap<String, Integer> orderDetails = new HashMap<>();
        orderDetails.put("F001", 2);
        
        dataService.saveOrder("customer@gmail.com", orderDetails);
        
        // Since the actual saving is done via utility, ensure the method was called
        verify(utility, atLeastOnce()).writeJSONFileForOrders(anyString(), any());
    }
    
    @Test
    void testCheckCustomer() {
        JSONArray mockCustomers = new JSONArray();
        JSONObject customer = new JSONObject();
        customer.put("mailId", "customer@gmail.com");
        mockCustomers.put(customer);
        
        when(utility.readJSONFile(anyString(), eq("customers"))).thenReturn(mockCustomers);
        
        assertTrue(dataService.checkCustomer("customer@gmail.com"));
        assertFalse(dataService.checkCustomer("unknown@gmail.com"));
    }
    
    @Test
    void testSaveCustomerDetails() {
        JSONArray mockCustomers = new JSONArray();
        when(utility.readJSONFile(anyString(), eq("customers"))).thenReturn(mockCustomers);
        
        boolean result = dataService.saveCustomerDetails("Genghis khan", "gk@gmail.com");
        assertFalse(result);
        
        verify(utility, atLeastOnce()).writeJSONFileForCustomers(anyString(), any());
    }
    
    @Test
    void testGenerateReport() {
        when(utility.readJSONFile(anyString(), eq("orders"))).thenReturn(new JSONArray());
        
        dataService.generateReport();
        
        verify(utility, atLeastOnce()).writeReport(any());
    }
}
