package com.pratap.estore.product.query;

import com.pratap.estore.product.core.events.ProductCreatedEvent;
import com.pratap.estore.product.data.ProductEntity;
import com.pratap.estore.product.data.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * This class is responsible to handle the Product Created Events, to save  in to database through spring data jpa
 */

@Component
@Slf4j
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent){

        log.info("Going to execute on() with productCreatedEvent = {}", productCreatedEvent);

        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, productEntity);

        log.info("calling ProductRepository save() with productEntity = {}", productEntity);
        productRepository.save(productEntity);
    }
}
