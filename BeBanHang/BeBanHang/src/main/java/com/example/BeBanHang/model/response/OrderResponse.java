package com.example.BeBanHang.model.response;

import com.example.BeBanHang.config.OrderStatus;
import com.example.BeBanHang.model.OrderItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private UserResponse user;
    private OrderStatus orderStatus;
    private Double totalAmount;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime expirationDate; // Thêm field này
    private boolean isExpired;
    private List<OrderItemResponse> orderItems;

}