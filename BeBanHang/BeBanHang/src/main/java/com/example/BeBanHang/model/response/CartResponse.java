package com.example.BeBanHang.model.response;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double total;
}
