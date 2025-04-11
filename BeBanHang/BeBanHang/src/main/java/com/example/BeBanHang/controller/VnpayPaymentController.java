package com.example.BeBanHang.controller;


import com.example.BeBanHang.service.OrderService;
import com.example.BeBanHang.service.VnPayPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
//	9704198526191432198
// NGUYEN VAN A
// 07/15
@RestController
@RequestMapping("/api/vnpay")
@Slf4j
public class VnpayPaymentController {

    @Autowired
    private VnPayPaymentService vnpayPaymentService;
    @Autowired
    private OrderService orderService;

    @GetMapping("/test-redirect")
    public ResponseEntity<Void> testRedirect() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:3000/orders"))
                .build();
    }

    @GetMapping("/return")
    public ResponseEntity<?> handleReturn(@RequestParam Map<String, String> params) throws Exception {

        String vnpResponseCode = params.get("vnp_ResponseCode");
        long orderId =Long.parseLong(params.get("vnp_TxnRef"));
        log.info("Response from VNPAY: " + params);
        if ("00".equals(vnpResponseCode)) {
            orderService.handleSuccessCheckout(orderId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:3000/orders"))
                    .build();
        } else {
            orderService.handleFailedCheckout(orderId);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create("http://localhost:3000/orders"))
                    .build();
        }

    }
    @GetMapping("/createPayUrl/{orderId}")
    public ResponseEntity<String > getPayUrlByOrderId(@PathVariable("orderId") Long orderId) throws Exception {
        return new ResponseEntity<>(orderService.createPaymentByOrderId(orderId) , HttpStatus.OK);
    }

    @PostMapping("/ipn")
    public String handleIpn(@RequestParam Map<String, String> params) {
        return "IPN received";
    }
}