package com.example.BeBanHang.mapper;

import com.example.BeBanHang.model.Order;
import com.example.BeBanHang.model.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring" ,uses = {UserMapper.class , OrderItemMapper.class})
public interface OrderMapper {
    Order INSTANCE = Mappers.getMapper(Order.class);

    @Mapping(source = "user", target = "user")
    @Mapping(source = "status" , target = "orderStatus")
    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "createdAt", target = "createdAt")
    OrderResponse orderToOrderResponse (Order order);
}
