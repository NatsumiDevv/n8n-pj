package com.example.BeBanHang.repository;
import com.example.BeBanHang.model.Cart;
import com.example.BeBanHang.model.User;
import com.example.BeBanHang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User  user);
    Optional<Cart> findByUserAndProduct(User user, Product product);
    Optional<Cart> findByIdAndUser(Long cartId, User user);
}
