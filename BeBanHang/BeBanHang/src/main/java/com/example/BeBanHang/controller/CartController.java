package com.example.BeBanHang.controller;

import com.example.BeBanHang.model.request.CartRequest;
import com.example.BeBanHang.model.response.CartResponse;
import com.example.BeBanHang.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request) {
        CartResponse cartResponse = cartService.addToCart(request);
        return ResponseEntity.ok(cartResponse);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart( @RequestParam Long cartId,@RequestParam Long userId
                                                ) {
        return ResponseEntity.ok(cartService.removeFromCart(cartId, userId));
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponse>> getCartItems(@PathVariable Long userId) {
        List<CartResponse> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }
}
