package com.example.BeBanHang.mapper;

import com.example.BeBanHang.model.Cart;
import com.example.BeBanHang.model.Product;
import com.example.BeBanHang.model.User;
import com.example.BeBanHang.model.response.CartResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-15T00:31:04+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponse toCartResponse(Cart cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartResponse.CartResponseBuilder cartResponse = CartResponse.builder();

        cartResponse.userId( cartItemUserId( cartItem ) );
        cartResponse.productId( cartItemProductId( cartItem ) );
        cartResponse.productName( cartItemProductName( cartItem ) );
        cartResponse.productPrice( cartItemProductPrice( cartItem ) );
        cartResponse.id( cartItem.getId() );
        cartResponse.quantity( cartItem.getQuantity() );
        cartResponse.total( cartItem.getTotal() );

        return cartResponse.build();
    }

    private Long cartItemUserId(Cart cart) {
        User user = cart.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private Long cartItemProductId(Cart cart) {
        Product product = cart.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String cartItemProductName(Cart cart) {
        Product product = cart.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }

    private Double cartItemProductPrice(Cart cart) {
        Product product = cart.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getPrice();
    }
}
