package com.pratap.estore.product.command.controller;

import com.pratap.estore.product.command.CreateProductCommand;
import com.pratap.estore.product.request.CreateProductRequestModel;
import com.pratap.estore.product.utils.JsonPrettyPrint;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductCommandController {

    private final CommandGateway commandGateway;

    @Autowired
    public ProductCommandController(CommandGateway commandGateway){
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createProduct(@RequestBody CreateProductRequestModel createProductRequestModel){

        log.info("Executing createProduct(), with createProductRequestModel = {}", JsonPrettyPrint.prettyPrint(createProductRequestModel));
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(createProductRequestModel.getPrice())
                .quantity(createProductRequestModel.getQuantity())
                .title(createProductRequestModel.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();

        log.info("Build createProductCommand from createProductRequestModel, createProductCommand = {}", JsonPrettyPrint.prettyPrint(createProductCommand));
        String returnValue;

        try {
            returnValue = commandGateway.sendAndWait(createProductCommand);
            log.info("returnValue =, {}", returnValue);
        } catch (CommandExecutionException exception){
            returnValue = exception.getLocalizedMessage();
            throw new CommandExecutionException("caught exception", exception);
        }
        return returnValue;
    }
}
