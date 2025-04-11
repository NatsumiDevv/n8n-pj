package com.example.BeBanHang.mapper;

import com.example.BeBanHang.model.OrderItem;
import com.example.BeBanHang.model.response.OrderItemResponse;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-15T00:31:05+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class OrderItemMapperImpl implements OrderItemMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemResponse orderItemResponse = new OrderItemResponse();

        orderItemResponse.setId( orderItem.getId() );
        orderItemResponse.setProduct( productMapper.toProductResponse( orderItem.getProduct() ) );
        orderItemResponse.setQuantity( orderItem.getQuantity() );
        orderItemResponse.setTotal( orderItem.getTotal() );

        return orderItemResponse;
    }
}
