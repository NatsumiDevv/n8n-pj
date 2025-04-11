package com.example.BeBanHang.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    public String code;
    public String message;
    public String paymentUrl;
}
