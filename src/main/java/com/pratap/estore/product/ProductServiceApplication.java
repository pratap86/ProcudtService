package com.pratap.estore.product;

import com.pratap.estore.product.command.interceptors.CreateProductCommandInterceptor;
import com.pratap.estore.product.core.errorhandling.ProductServiceEventsErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	// register CreateProductCommandInterceptor, interceptor in to command bus through application context
	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext applicationContext, CommandBus commandBus){
		log.info("Executing registerCreateProductCommandInterceptor() to inject interceptor with applicationContext={} and commandBus={}", applicationContext, commandBus);
		commandBus.registerDispatchInterceptor(applicationContext.getBean(CreateProductCommandInterceptor.class));
	}

	// register ProductServiceEventsErrorHandler through EventProcessingConfigurer
	@Autowired
	public void config(EventProcessingConfigurer config){
		config.registerListenerInvocationErrorHandler("product-group",
				configuration -> new ProductServiceEventsErrorHandler());
//		config.registerListenerInvocationErrorHandler("product-group",
//				configuration -> PropagatingErrorHandler.instance());
	}
}
