package org.vsu.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.vsu.orderservice.entity.CartItem;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private String key(String buyerId) {
        return "cart:" + buyerId;
    }

    public List<CartItem> getAllCart(Jwt jwt) {
        String buyerId = jwt.getSubject();

        Map<Object, Object> medicines = redisTemplate.opsForHash().entries(key(buyerId));

        return medicines.values().stream()
                .map(value -> objectMapper.convertValue(value, CartItem.class))
                .toList();
    }

    public void removeListOfItems(Jwt jwt, List<CartItem> itemList) {
        String buyerId = jwt.getSubject();
        Object[] ids = itemList.stream().map(el -> el.getMedicineId().toString()).toArray();

        redisTemplate.opsForHash().delete(key(buyerId), ids);
    }

    public void addItem(Jwt jwt, CartItem item) {
        try {
            String buyerId = jwt.getSubject();
            String medicineId = item.getMedicineId().toString();

            redisTemplate.opsForHash().put(key(buyerId), medicineId, objectMapper.writeValueAsString(item));
            redisTemplate.expire(key(buyerId), Duration.ofDays(7));
        } catch (Exception e) {
            //TODO: Заменить на свой FaildeToAddItemException()
            throw new RuntimeException("500");
        }
    }

    public void removeItem(Jwt jwt, CartItem item) {
        String buyerId = jwt.getSubject();
        String medicineId = item.getMedicineId().toString();

        redisTemplate.opsForHash().delete(key(buyerId), medicineId);
    }

    public void clearCart(Jwt jwt) {
        String buyerId = jwt.getSubject();
        redisTemplate.delete(key(buyerId));
    }

}
