package com.example.eventsourcing.model;

import java.io.Serializable;
import java.util.Objects;

public class CartEvent implements Serializable {

    public enum EventType {
        ADD_ITEM, REMOVE_ITEM
    }

    private EventType type;
    private String itemId;
    private int quantity;
    private long timestamp;

    // Default constructor for serialization
    public CartEvent() {}

    public CartEvent(EventType type, String itemId, int quantity) {
        this.type = type;
        this.itemId = itemId;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CartEvent{" +
                "type=" + type +
                ", itemId='" + itemId + '\'' +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}