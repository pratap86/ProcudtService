package com.pratap.estore.product.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequestModel {

    private String title;
    private BigDecimal price;
    private Integer quantity;
}
