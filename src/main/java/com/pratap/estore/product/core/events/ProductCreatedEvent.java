package com.pratap.estore.product.core.events;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Naming Convention <Noun><PerformedAction>Event
 */
@Data
public class ProductCreatedEvent {

    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;
}
