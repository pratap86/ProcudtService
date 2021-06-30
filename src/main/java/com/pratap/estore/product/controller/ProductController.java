package com.pratap.estore.product.controller;

import com.pratap.estore.product.command.CreateProductCommand;
import com.pratap.estore.product.request.CreateProductRequestModel;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CommandGateway commandGateway;

    @Autowired
    public ProductController(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @GetMapping
    public String getProduct(){
        return "Get Mapping";
    }

    @PostMapping
    public String createProduct(@RequestBody CreateProductRequestModel createProductRequestModel){

        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(createProductRequestModel.getPrice())
                .quantity(createProductRequestModel.getQuantity())
                .title(createProductRequestModel.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue;

        try {
            returnValue = commandGateway.sendAndWait(createProductCommand);
        } catch (CommandExecutionException exception){
            returnValue = exception.getLocalizedMessage();
            throw new CommandExecutionException("caught exception", exception);
        }
        return returnValue;
    }
}
