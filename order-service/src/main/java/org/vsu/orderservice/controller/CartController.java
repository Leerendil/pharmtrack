package org.vsu.orderservice.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.vsu.orderservice.entity.CartItem;
import org.vsu.orderservice.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getAllItems(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(cartService.getAllCart(jwt));
    }

    @PostMapping
    public ResponseEntity<Boolean> addItem(@RequestBody @Valid CartItem item, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(jwt, item));
    }

    @DeleteMapping("/selected")
    public ResponseEntity<Boolean> removeListOfItems(@AuthenticationPrincipal  Jwt jwt, @RequestBody int[] ids) {
        return ResponseEntity.ok(cartService.removeListOfItems(jwt, ids));
    }

    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Boolean> removeItem(@AuthenticationPrincipal  Jwt jwt, @PathVariable("medicineId") String medicineId) {
        return ResponseEntity.ok(cartService.removeItem(jwt, medicineId));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> clearCart(@AuthenticationPrincipal  Jwt jwt) {
        return ResponseEntity.ok(cartService.clearCart(jwt));
    }
}
