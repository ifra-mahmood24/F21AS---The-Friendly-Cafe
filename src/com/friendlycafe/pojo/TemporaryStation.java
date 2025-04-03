package com.friendlycafe.pojo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TemporaryStation {
    private static final int MAX_CAPACITY = 5;
    private Queue<Order> orderQueue;

    public TemporaryStation() {
        this.orderQueue = new LinkedList<>();
    }

    public synchronized boolean addOrder(Order order) {
        if (orderQueue.size() >= MAX_CAPACITY) {
            return false; // Station is full
        }
        orderQueue.add(order);
        return true;
    }

    public synchronized Order getNextOrder() {
        return orderQueue.poll();
    }

    public synchronized boolean isFull() {
        return orderQueue.size() >= MAX_CAPACITY;
    }

    public synchronized boolean isEmpty() {
        return orderQueue.isEmpty();
    }

    public synchronized int getCurrentSize() {
        return orderQueue.size();
    }

    public synchronized List<Order> getAllOrders() {
        return new ArrayList<>(orderQueue); 
    }
}
