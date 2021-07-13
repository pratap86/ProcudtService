package com.pratap.estore.product.command.handlers;

import com.pratap.estore.product.core.events.ProductCreatedEvent;
import com.pratap.estore.product.core.data.ProductEntity;
import com.pratap.estore.product.core.data.repository.ProductRepository;
import com.pratap.estore.shared.events.ProductReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * This class is responsible to handle the Product Created Events, to save  in to database through spring data jpa
 */

@Slf4j
@Component
@ProcessingGroup("product-group")
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

    // update the read data base
    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){

        log.info("ProductReservedEvent is called for orderId={}", productReservedEvent.getOrderId());

        ProductEntity productEntity = productRepository.getById(productReservedEvent.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
        log.info("Going to update read data base by on() with productReservedEvent={}", productReservedEvent);
        productRepository.save(productEntity);

    }
}
