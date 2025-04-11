package com.example.BeBanHang.mapper;

import com.example.BeBanHang.model.Product;
import com.example.BeBanHang.model.response.ProductResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-15T00:31:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse toProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.name( product.getName() );
        productResponse.price( product.getPrice() );
        productResponse.id( product.getId() );

        return productResponse.build();
    }
}
