package com.example.BeBanHang.service;


import com.example.BeBanHang.mapper.ProductMapper;
import com.example.BeBanHang.model.Product;
import com.example.BeBanHang.model.response.ProductResponse;
import com.example.BeBanHang.repository.ProductRepository;
import com.example.BeBanHang.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;

//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }

    public ProductResponse getProductById(Long id ){
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy sản phẩm"));
        return productMapper.toProductResponse(product);
    }

    public List<ProductResponse> getAllProducts (){
        List<Product> listProducts = productRepository.findAll();
        List<ProductResponse> listProductResponse  = new ArrayList<>();
        for(Product pr : listProducts){
            ProductResponse productResponse = ProductResponse.builder()
                    .id(pr.getId())
                    .name(pr.getName())
                    .price(pr.getPrice())
                    .build();
            listProductResponse.add(productResponse);
        }
        return listProductResponse;
    }

    public Product saveProduct(String name, Double price, MultipartFile file) throws IOException {
        System.out.println(name);
        Product pr = new Product();
        pr.setName(name);
        pr.setPrice(price);
        pr.setImage(ImageUtils.compressImage(file.getBytes()));
        productRepository.save(pr);
        return pr;
    }

    public byte[] getProductImage(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return ImageUtils.decompressImage(product.get().getImage());
        }
        return null;
    }
}