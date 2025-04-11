package com.example.BeBanHang.controller;

import com.example.BeBanHang.config.OrderStatus;
import com.example.BeBanHang.model.request.OrderRequest;
import com.example.BeBanHang.model.response.OrderResponse;
import com.example.BeBanHang.service.OrderService;
import com.example.BeBanHang.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<String> checkout(@PathVariable Long userId) throws Exception {
        return new ResponseEntity<>(orderService.checkout(userId), HttpStatus.CREATED);
    }

    @GetMapping("/getorder/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.getOrder(orderId), HttpStatus.OK);
    }

    @GetMapping("/getAllByUserId")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@RequestParam Long userId) {
        List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getOrdersByStatus() {
        List<OrderResponse> orders = orderService.getAllOrderByStatusPendingAndFailed();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Xóa Thành Công");
        } catch (RuntimeException e) {
            log.error("Lỗi khi xóa order với ID {}: {}", orderId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 nếu không tìm thấy
        }
    }

}