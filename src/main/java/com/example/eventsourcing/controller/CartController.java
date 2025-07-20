package com.example.eventsourcing.controller;

import com.example.eventsourcing.model.CartEvent;
import com.example.eventsourcing.service.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addItem(@PathVariable String userId, @RequestBody CartEvent event) {
        if (event.getType() != CartEvent.EventType.ADD_ITEM) {
            return ResponseEntity.badRequest().body("Invalid event type for this endpoint.");
        }
        try {
            cartService.recordEvent(userId, event);
            return ResponseEntity.ok("Item added to cart.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/remove")
    public ResponseEntity<String> removeItem(@PathVariable String userId, @RequestBody CartEvent event) {
        if (event.getType() != CartEvent.EventType.REMOVE_ITEM) {
            return ResponseEntity.badRequest().body("Invalid event type for this endpoint.");
        }
        try {
            cartService.recordEvent(userId, event);
            return ResponseEntity.ok("Item removed from cart.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Integer>> getCartState(@PathVariable String userId) {
        try {
            Map<String, Integer> cartState = cartService.getCurrentCartState(userId);
            return ResponseEntity.ok(cartState);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        cartService.clearCartHistory(userId);
        return ResponseEntity.ok("Cart history cleared.");
    }
}