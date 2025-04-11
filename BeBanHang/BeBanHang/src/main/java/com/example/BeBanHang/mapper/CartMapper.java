package com.example.BeBanHang.mapper;


import com.example.BeBanHang.model.Cart;
import com.example.BeBanHang.model.response.CartResponse;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "cartItem.user.id", target = "userId")
    @Mapping(source = "cartItem.product.id", target = "productId")
    @Mapping(source = "cartItem.product.name", target = "productName")
    @Mapping(source = "cartItem.product.price", target = "productPrice")
    CartResponse toCartResponse(Cart cartItem);
}
