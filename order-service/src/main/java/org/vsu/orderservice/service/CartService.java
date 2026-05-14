package org.vsu.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.vsu.orderservice.clients.CatalogClientService;
import org.vsu.orderservice.dto.MedicineBasicInfo;
import org.vsu.orderservice.entity.CartItem;
import org.vsu.orderservice.utils.exceptions.FailedToAddItemException;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CatalogClientService catalogClientService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private String key(String buyerId) {
        return "cart:" + buyerId;
    }

    public List<CartItem> getAllCart(Jwt jwt) {
        String buyerId = jwt.getSubject();

        Map<Object, Object> medicines = redisTemplate.opsForHash().entries(key(buyerId));

        return medicines.values().stream()
                .map(value -> {
                    try {
                        return objectMapper.readValue(value.toString(), CartItem.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    public boolean removeListOfItems(Jwt jwt, int[] ids) {
        String buyerId = jwt.getSubject();

        Object[] stringIds = Arrays.stream(ids)
                .mapToObj(String::valueOf)
                .toArray();

        redisTemplate.opsForHash().delete(key(buyerId), stringIds);

        return true;
    }

    public boolean addItem(Jwt jwt, CartItem item) {
        MedicineBasicInfo medicineBasicInfo = catalogClientService.getById(item.getMedicineId()).getBody();

        if (medicineBasicInfo.getPrice().compareTo(item.getPrice()) != 0) {
            return false;
        }

        try {
            String buyerId = jwt.getSubject();
            String medicineId = item.getMedicineId().toString();

            redisTemplate.opsForHash().put(key(buyerId), medicineId, objectMapper.writeValueAsString(item));
            redisTemplate.expire(key(buyerId), Duration.ofDays(7));
        } catch (Exception e) {
            throw new FailedToAddItemException(e);
        }

        return true;
    }

    public boolean removeItem(Jwt jwt, String medicineId) {
        String buyerId = jwt.getSubject();

        redisTemplate.opsForHash().delete(key(buyerId), medicineId);

        return true;
    }

    public boolean clearCart(Jwt jwt) {
        String buyerId = jwt.getSubject();
        redisTemplate.delete(key(buyerId));

        return true;
    }

}
