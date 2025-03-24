/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package productx;

/**
 *
 * @author annae
 */


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cart {
    private static volatile Cart instance;
    private Map<String, CheckoutBuy.ProductItem> items;
    
    private Cart() {
        // Use ConcurrentHashMap for thread safety
        items = new ConcurrentHashMap<>();
    }
    
    public static synchronized Cart getInstance() {
        if (instance == null) {
            synchronized (Cart.class) {
                if (instance == null) {
                    instance = new Cart();
                }
            }
        }
        return instance;
    }
    
    public void addItem(String pid, CheckoutBuy.ProductItem item) {
        synchronized (this) {
            if (items.containsKey(pid)) {
                CheckoutBuy.ProductItem existingItem = items.get(pid);
                existingItem.quantity += item.quantity;
                if (existingItem.quantity > existingItem.availableQuantity) {
                    existingItem.quantity = existingItem.availableQuantity;
                }
            } else {
                items.put(pid, item);
            }
        }
    }
    
    public void removeItem(String pid) {
        synchronized (this) {
            items.remove(pid);
        }
    }
    
    public void updateItemQuantity(String pid, int quantity) {
        synchronized (this) {
            if (items.containsKey(pid)) {
                CheckoutBuy.ProductItem item = items.get(pid);
                item.quantity = quantity;
                if (item.quantity > item.availableQuantity) {
                    item.quantity = item.availableQuantity;
                }
            }
        }
    }
    
    public void clear() {
        synchronized (this) {
            items.clear();
            System.out.println("Cart cleared. Item count: " + items.size()); // Debug print
        }
    }
    
    public Map<String, CheckoutBuy.ProductItem> getItems() {
        return new HashMap<>(items); // Return a copy to prevent direct modification
    }
    
    public int getItemCount() {
        return items.size();
    }
    
    public double getTotalPrice() {
        double total = 0;
        for (CheckoutBuy.ProductItem item : items.values()) {
            total += item.price * item.quantity;
        }
        return total;
    }

    // Add a method to reset the singleton instance (useful for testing)
    public static synchronized void resetInstance() {
        instance = null;
    }
}