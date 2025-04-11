package com.example.BeBanHang.repository;


import com.example.BeBanHang.config.OrderStatus;
import com.example.BeBanHang.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long userId, Sort sort);

    @Query("SELECT o FROM Order o WHERE o.status IN (:status)")
    List<Order> findOrdersByStatusUsingQuery (@Param("status")List<OrderStatus> status);
}