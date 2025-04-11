package com.example.BeBanHang.mapper;

import com.example.BeBanHang.model.Product;
import com.example.BeBanHang.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

@Mapping(source = "product.name" , target = "name")
    @Mapping(source = "product.price" , target = "price")
    ProductResponse toProductResponse (Product product);
}
