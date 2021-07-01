package com.pratap.estore.product.query;

import com.pratap.estore.product.core.events.ProductCreatedEvent;
import com.pratap.estore.product.data.ProductEntity;
import com.pratap.estore.product.data.ProductRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * This class is responsible to handle the Product Created Events
 */

@Component
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent){

        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, productEntity);

        productRepository.save(productEntity);
    }
}
