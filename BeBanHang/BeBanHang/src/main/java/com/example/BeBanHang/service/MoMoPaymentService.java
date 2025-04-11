package com.example.BeBanHang.service;

import com.example.BeBanHang.model.request.PaymentRequest;
import com.example.BeBanHang.utils.MoMoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;
@Slf4j
@Service
public class MoMoPaymentService {
//    captureWallet
//    payWithATM
    @Value("${momo.partner-code}")
    private String partnerCode;

    @Value("${momo.access-key}")
    private String accessKey;

    @Value("${momo.secret-key}")
    private String secretKey;

    @Value("${momo.end-point}")
    private String endpoint;

    @Value("${momo.return-url}")
    private String returnUrl;

    @Value("${momo.ipn-url}")
    private String ipnUrl;

    @Value("${momo.request-type}")
    private String requestType;

    private final RestTemplate restTemplate;

    public MoMoPaymentService() {
        this.restTemplate = new RestTemplate();
    }


    public String createPayment(long total, String orderInfo) throws Exception {
        String requestId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();
        String extraData = "";
        String rawData = "accessKey=" + accessKey +
                "&amount=" + total +
                "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode +
                "&redirectUrl=" + returnUrl +
                "&requestId=" + requestId +
                "&requestType=" + requestType;
        String signature = MoMoUtils.generateSignature(rawData, secretKey);
        if(signature.isBlank()){
            log.info("Signature rong! ");
            return null;
        }
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .partnerCode(partnerCode)
                .requestId(requestId)
                .orderId(orderId)
                .amount(total)
                .orderInfo(orderInfo)
                .redirectUrl(returnUrl)
                .ipnUrl(ipnUrl)
                .requestType(requestType)
                .extraData(extraData)
                .signature(signature)
                .lang("vi")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(paymentRequest, headers);
        String jsonResponse = restTemplate.postForObject(endpoint, requestEntity, String.class);

        return jsonResponse;
    }


}