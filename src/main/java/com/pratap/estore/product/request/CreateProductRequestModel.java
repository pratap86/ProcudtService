package com.pratap.estore.product.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CreateProductRequestModel {

    @NotBlank(message = "Product title is a required field")
    private String title;

    @Min(value = 1, message = "price can not be lower than 1")
    private BigDecimal price;

    @Min(value = 1, message = "quantity can not be lower than 1")
    @Max(value = 5, message = "quantity can not be larger than 5")
    private Integer quantity;
}
