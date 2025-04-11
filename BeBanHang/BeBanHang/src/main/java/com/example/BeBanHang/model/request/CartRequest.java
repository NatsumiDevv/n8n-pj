package com.example.BeBanHang.model.request;


import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
