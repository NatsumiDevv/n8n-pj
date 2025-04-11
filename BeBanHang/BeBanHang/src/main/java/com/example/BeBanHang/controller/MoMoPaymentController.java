package com.example.BeBanHang.controller;

import com.example.BeBanHang.service.MoMoPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoMoPaymentController {

    @Autowired
    private MoMoPaymentService moMoPaymentService;

    @GetMapping(value = "/api/momo/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createPayment(@RequestParam long total, @RequestParam String orderInfo) throws Exception {
        return moMoPaymentService.createPayment(total, orderInfo);
    }

    @GetMapping("/api/momo/return")
    public String handleReturn() {
        return "Payment completed!";
    }

    @PostMapping("/api/momo/ipn-handler")
    public void handleNotify() {
    }
}