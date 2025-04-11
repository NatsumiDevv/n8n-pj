package com.example.BeBanHang.controller;

import com.example.BeBanHang.service.WebhookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookTestService) {
        this.webhookService = webhookTestService;
    }

    @GetMapping("/send-test")
    public String sendTest() {
        webhookService.testSendDataToN8n();
        return "Đã gửi dữ liệu lên n8n!";
    }
}