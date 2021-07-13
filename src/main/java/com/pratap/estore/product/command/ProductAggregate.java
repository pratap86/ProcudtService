package com.pratap.estore.product.command;

import com.pratap.estore.product.command.commands.CreateProductCommand;
import com.pratap.estore.product.core.events.ProductCreatedEvent;
import com.pratap.estore.product.utils.JsonPrettyPrint;
import com.pratap.estore.shared.commands.ProductReservedCommand;
import com.pratap.estore.shared.events.ProductReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * This class represent the current state of Main Object ie Product is main object
 * @author Pratap Narayan
 */

@Aggregate
@Slf4j
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate(){}

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand){
        log.info("Executing ProductAggregate() with CreateProductCommand = {}", JsonPrettyPrint.prettyPrint(createProductCommand));
        // validate createProductCommand
        validateCreateProductCommand(createProductCommand);

        // publish the product create event
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    private void validateCreateProductCommand(CreateProductCommand createProductCommand) {
        log.info("Executing validateCreateProductCommand() of createProductCommand = {}", JsonPrettyPrint.prettyPrint(createProductCommand));
        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0){
            log.error("BAD Request", new IllegalArgumentException("Price can not be less or equal to be zero"));
            throw new IllegalArgumentException("Price can not be less or equal to be zero");
        }
        if (createProductCommand.getTitle() == null
            || createProductCommand.getTitle().isBlank()){
            log.error("BAD Request", new IllegalArgumentException("Title can not be empty"));
            throw new IllegalArgumentException("Title can not be empty");
        }
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){

        log.info("Executing on() with productCreatedEvent = {}", productCreatedEvent);
            this.productId = productCreatedEvent.getProductId();
            this.price = productCreatedEvent.getPrice();
            this.title = productCreatedEvent.getTitle();
            this.quantity = productCreatedEvent.getQuantity();
    }

    @CommandHandler
    public void handle(ProductReservedCommand productReservedCommand){

        if (quantity < productReservedCommand.getQuantity())
            throw new IllegalArgumentException("Insufficient number of items in stock");

        ProductReservedEvent reserveProductEvent = ProductReservedEvent.builder()
                .orderId(productReservedCommand.getOrderId())
                .productId(productReservedCommand.getProductId())
                .quantity(productReservedCommand.getQuantity())
                .userId(productReservedCommand.getUserId())
                .build();

        AggregateLifecycle.apply(reserveProductEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent){
        this.quantity -= productReservedEvent.getQuantity();
    }
}
