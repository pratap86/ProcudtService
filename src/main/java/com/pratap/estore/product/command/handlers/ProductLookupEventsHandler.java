package com.pratap.estore.product.command.handlers;

import com.pratap.estore.product.core.events.ProductCreatedEvent;
import com.pratap.estore.product.data.ProductLookupEntity;
import com.pratap.estore.product.data.repository.ProductLookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ProcessingGroup("product-group")
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository){
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event){

        log.info("Executing on() with event={}", event);
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        log.info("going to intercept the event handler to check is record exist");
        log.info("Going to save productLookupEntity={} into productlookup table", productLookupEntity);
        productLookupRepository.save(productLookupEntity);
    }
}
