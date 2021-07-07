package com.pratap.estore.product.query.controller;

import com.pratap.estore.product.query.FindProductQuery;
import com.pratap.estore.product.responce.ProductResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<ProductResponseModel> getProducts(){

        log.info("Executing Action=\"{}\"", "getProducts()");
        FindProductQuery findProductQuery = new FindProductQuery();
        return queryGateway.query(findProductQuery, ResponseTypes.multipleInstancesOf(ProductResponseModel.class)).join();
    }
}
