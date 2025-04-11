package com.example.BeBanHang.service;

import com.example.BeBanHang.config.OrderStatus;
import com.example.BeBanHang.mapper.OrderMapper;
import com.example.BeBanHang.model.*;
import com.example.BeBanHang.model.request.OrderRequest;
import com.example.BeBanHang.model.response.OrderResponse;
import com.example.BeBanHang.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VnPayPaymentService vnPayPaymentService;

    @Autowired
    private OrderMapper orderMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${n8n.url-n8n}")
    private String url_n8n;

    @Value("${n8n.url-n8n-production}")
    private  String urlN8nProduction;


    public List<OrderResponse> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUser_Id(userId,Sort.by(Sort.Direction.DESC, "id"));
        if (orders.isEmpty()) {
            throw new RuntimeException("Không có đơn hàng nào cho userId: " + userId);
        }
        return orders.stream()
                .map(orderMapper::orderToOrderResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrderByStatusPendingAndFailed(){
        List<OrderStatus> statuses = Arrays.asList(OrderStatus.PENDING, OrderStatus.FAILED);
        List<Order> orders = orderRepository.findOrdersByStatusUsingQuery(statuses);
        if(orders.isEmpty()){
            log.info("Danh sách order theo status rỗng");
            return null;
        }
        return orders.stream()
                .map(order -> {
                    OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);
                    LocalDateTime expirationDate = order.getCreatedAt().plusMinutes(10); 
                    orderResponse.setExpirationDate(expirationDate);
                    orderResponse.setExpired(LocalDateTime.now().isAfter(expirationDate));
                    return orderResponse;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public String checkout(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User không tồn tại !"));
        List<Cart> listCarts = cartRepository.findByUser(user);
        if (listCarts.isEmpty()) {
            throw new RuntimeException("Giỏ hàng rỗng!!");
        }

        double totalAmount = listCarts.stream()
                .mapToDouble(cart -> cart.getProduct().getPrice() * cart.getQuantity())
                .sum();

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .build();

        orderRepository.save(order);

        List<OrderItem> orderItems = listCarts.stream()
                .map(cart -> {
                    OrderItem orderItem = OrderItem.builder()
                            .order(order)
                            .total(cart.getTotal())
                            .product(cart.getProduct())
                            .quantity(cart.getQuantity())
                            .build();
                    return orderItem;
                }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderItemRepository.saveAll(orderItems);
        cartRepository.deleteAll(listCarts);

        String payUrl = vnPayPaymentService.createPayment(orderMapper.orderToOrderResponse(order));

        return payUrl;
    }

    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order không tồn tại!"));
        if (order.getOrderItems().isEmpty()) {
            log.info("Danh sach order rong !");
            return null;
        }
        OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);
        LocalDateTime expirationDate = order.getCreatedAt().plusMinutes(10);
        orderResponse.setExpirationDate(expirationDate);
        orderResponse.setExpired(LocalDateTime.now().isAfter(expirationDate));
        return orderResponse;
    }


    public void sendMailOrderN8n(OrderResponse orderResponse) throws Exception {
        User user = userRepository.findById(orderResponse.getUser().getId()).orElseThrow(()->new RuntimeException("User không tồn tại!"));
        Map<String, Object > req = new HashMap<>();
        req.put("email", user.getEmail() );
        req.put("name" , user.getName());
        req.put("order" , orderResponse);
        if(orderResponse.getOrderStatus().getValue() == 2){
            String payUrl = vnPayPaymentService.createPayment(orderResponse);
            req.put("payUrl", payUrl);
            log.info("Đã gửi payUrl");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String , Object>> entity = new HttpEntity<>(req, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(urlN8nProduction, entity, String.class);
        System.out.println("Gửi đơn hàng thành công: " + response.getBody());
    }
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            order.setStatus(orderStatus);
            order = orderRepository.save(order);
            OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);
            return orderResponse;
        }
        throw new RuntimeException("Order not found");
    }

    public void handleSuccessCheckout(Long orderId) throws Exception {
       OrderResponse orderResponse = updateOrderStatus(orderId, OrderStatus.PAID);
       sendMailOrderN8n(orderResponse);
    }

    public void handleFailedCheckout(Long orderId) throws Exception {
        OrderResponse orderResponse = updateOrderStatus(orderId, OrderStatus.FAILED);
        sendMailOrderN8n(orderResponse);
    }

    public String createPaymentByOrderId(Long orderId) throws Exception {
        OrderResponse orderResponse = getOrder(orderId);
        if(orderResponse.getOrderStatus().getValue()==1){
            throw new RuntimeException("Đã thanh toán!");
        }
        return vnPayPaymentService.createPayment(orderResponse);
    }

    @Transactional
    public boolean deleteOrder(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.get().getStatus().getValue()==1){
            throw new RuntimeException("Đơn hàng đã thanh toán được!");
        }
        if (optionalOrder.isPresent()) {
            orderRepository.delete(optionalOrder.get());
            log.info("Đã xóa thành công order với ID: {}", orderId);
            return true;
        }
        log.warn("Order với ID {} không tồn tại!", orderId);
        return false;
    }
}
