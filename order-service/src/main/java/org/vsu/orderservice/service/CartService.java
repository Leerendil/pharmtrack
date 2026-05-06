package org.vsu.orderservice.service;

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
        return "cart: " + buyerId;
    }

    public List<CartItem> getAllCart(Jwt jwt) {
        String buyerId = jwt.getSubject();

        Map<Object, Object> medicines = redisTemplate.opsForHash().entries(key(buyerId));

        return medicines.values().stream()
                .map(value -> objectMapper.convertValue(value, CartItem.class))
                .toList();
    }

    public void addItem(Jwt jwt, CartItem item) {
        String buyerId = jwt.getSubject();
        String medicineId = item.getMedicineId().toString();

        redisTemplate.opsForHash().put(key(buyerId), medicineId, item.toString());
        redisTemplate.expire(key(buyerId), Duration.ofDays(7));
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
