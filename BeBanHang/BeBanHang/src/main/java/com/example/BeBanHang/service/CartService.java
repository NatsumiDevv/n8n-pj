package com.example.BeBanHang.service;
import com.example.BeBanHang.mapper.CartMapper;
import com.example.BeBanHang.model.Cart;
import com.example.BeBanHang.model.Product;
import com.example.BeBanHang.model.User;
import com.example.BeBanHang.model.request.CartRequest;
import com.example.BeBanHang.model.response.CartResponse;
import com.example.BeBanHang.repository.CartRepository;
import com.example.BeBanHang.repository.ProductRepository;
import com.example.BeBanHang.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    private final CartMapper cartMapper;
    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }
    public CartResponse addToCart(CartRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        Cart cartItem = cartRepository.findByUserAndProduct(user, product)
                .orElse(Cart.builder()
                        .user(user)
                        .product(product)
                        .quantity(0)
                        .total(0.0)
                        .createdTime(new Date())
                        .build());

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        cartItem.setTotal(cartItem.getQuantity() * product.getPrice());

        cartItem = cartRepository.save(cartItem);

        return cartMapper.toCartResponse(cartItem);
    }
    public CartResponse removeFromCart(Long cartId, Long userId) {
        log.info("CartId:"+cartId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Cart cartItem = cartRepository.findByIdAndUser(cartId, user)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng!!"));
        cartRepository.delete(cartItem);
        return cartMapper.toCartResponse(cartItem);
    }

    public List<CartResponse> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        return cartRepository.findByUser(user)
                .stream()
                .map(cartMapper::toCartResponse)
                .collect(Collectors.toList());
    }
}
