package com.example.eventsourcing.service;

import com.example.eventsourcing.model.CartEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final String CART_PREFIX = "cart:";
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CartService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getCartKey(String userId) {
        return CART_PREFIX + userId;
    }

    public void recordEvent(String userId, CartEvent event) throws JsonProcessingException {
        String eventJson = objectMapper.writeValueAsString(event);
        redisTemplate.opsForList().rightPush(getCartKey(userId), eventJson);
    }

    public Map<String, Integer> getCurrentCartState(String userId) throws JsonProcessingException {
        Map<String, Integer> cartState = new HashMap<>();

        // Get all events from the Redis list
        List<String> eventsJson = redisTemplate.opsForList().range(getCartKey(userId), 0, -1);

        if (eventsJson == null) {
            return cartState;
        }

        // Replay events to rebuild the state
        for (String eventJson : eventsJson) {
            CartEvent event = objectMapper.readValue(eventJson, CartEvent.class);

            switch (event.getType()) {
                case ADD_ITEM:
                    cartState.merge(event.getItemId(), event.getQuantity(), Integer::sum);
                    break;
                case REMOVE_ITEM:
                    int currentQuantity = cartState.getOrDefault(event.getItemId(), 0);
                    int newQuantity = Math.max(0, currentQuantity - event.getQuantity());
                    if (newQuantity == 0) {
                        cartState.remove(event.getItemId());
                    } else {
                        cartState.put(event.getItemId(), newQuantity);
                    }
                    break;
            }
        }
        return cartState;
    }

    public void clearCartHistory(String userId) {
        redisTemplate.delete(getCartKey(userId));
    }
}