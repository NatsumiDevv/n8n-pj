package com.example.BeBanHang.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_N8N= "http://localhost:1234/webhook-test/my-webhook";

    public void testSendDataToN8n (){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> request = new HttpEntity<>("Helllo", headers);

        ResponseEntity<String > responseEntity= restTemplate.postForEntity( URL_N8N,request, String.class);
        System.out.println("Trạng thái HTTP: " + responseEntity.getStatusCode());
        System.out.println("Phản hồi từ n8n: " + responseEntity.getBody());
    }

}
