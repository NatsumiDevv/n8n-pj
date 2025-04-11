package com.example.BeBanHang.mapper;

import com.example.BeBanHang.model.OrderItem;
import com.example.BeBanHang.model.response.OrderItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = {ProductMapper.class})
public interface OrderItemMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "total", source = "total")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
