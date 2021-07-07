package com.pratap.estore.product.query;

import com.pratap.estore.product.data.ProductEntity;
import com.pratap.estore.product.data.ProductRepository;
import com.pratap.estore.product.responce.ProductResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ProductQueryHandler {

    private final ProductRepository productRepository;

    @Autowired
    public ProductQueryHandler(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductResponseModel> findProducts(FindProductQuery query){

        log.info("Executing Action=\"{}\"", "findProducts()");
        List<ProductResponseModel> products = new ArrayList<>();
        List<ProductEntity> storedProducts = productRepository.findAll();
        log.info("storedProducts=\"{}\"", storedProducts);
        storedProducts.forEach(productEntity -> {
            ProductResponseModel productResponseModel = new ProductResponseModel();
            BeanUtils.copyProperties(productEntity, productResponseModel);
            products.add(productResponseModel);
        });
        return products;
    }
}
