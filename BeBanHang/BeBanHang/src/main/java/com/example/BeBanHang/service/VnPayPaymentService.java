package com.example.BeBanHang.service;


import com.example.BeBanHang.model.response.OrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
@Slf4j
@Service
public class VnPayPaymentService {

    @Value("${vnpay.tmn-code}")
    private String vnpTmnCode;

    @Value("${vnpay.hash-secret}")
    private String vnpHashSecret;

    @Value("${vnpay.vnp-command}")
    private String vnpCommand;

    @Value("${vnpay.vnp-order-type}")
    private String vnpOrderType;
    @Value("${vnpay.vnp-version}")
    private String vnpVersion;

    @Value("${vnpay.payment-url}")
    private String vnpPaymentUrl;

    @Value("${vnpay.return-url}")
    private String vnpReturnUrl;

    @Value("${vnpay.ipn-url}")
    private String vnpIpnUrl;

    public String createPayment(OrderResponse orderResponse) throws Exception {
        if (orderResponse == null || orderResponse.getId() == null || orderResponse.getTotalAmount() == null) {
            throw new IllegalArgumentException("Order information cannot be null");
        }
        String vnpIpAddr = "127.0.0.1";
        String vnpCreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        long amount= (long) (orderResponse.getTotalAmount() * 100L);
        String orderInfo = "Đơn hàng "+orderResponse.getId()+" có giá trị là: "+ orderResponse.getTotalAmount();
        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", orderResponse.getId().toString());
        vnpParams.put("vnp_OrderInfo", orderInfo);
        vnpParams.put("vnp_OrderType", vnpOrderType);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpReturnUrl);
        vnpParams.put("vnp_IpAddr", vnpIpAddr);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()))
                    .append("&");
        }
        String queryUrl = query.substring(0, query.length() - 1);

        // Tạo chữ ký
        String vnpSecureHash = hmacSHA512(vnpHashSecret, queryUrl);


        String paymentUrl = vnpPaymentUrl + "?" + queryUrl + "&vnp_SecureHash=" + vnpSecureHash;

        log.info("Tao payUrl thanh cong");

        return paymentUrl;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}