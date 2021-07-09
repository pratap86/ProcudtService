package com.pratap.estore.product.command.interceptors;

import com.pratap.estore.product.command.CreateProductCommand;
import com.pratap.estore.product.data.ProductLookupEntity;
import com.pratap.estore.product.data.repository.ProductLookupRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

/**
 * This interceptor would intercept the create product command.
 * Also this class would present in application context, use @Component
 */
@Slf4j
@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository){
        this.productLookupRepository = productLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            log.info("Interceptor called with Intercepted command={}", command.getPayload());
            if (CreateProductCommand.class.equals(command.getPayloadType())){

                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();

                ProductLookupEntity productLookupEntity = productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(), createProductCommand.getTitle());

                if (productLookupEntity != null){
                    throw new IllegalStateException(
                            String.format("Product with productId %s or title %s already exist",
                                    productLookupEntity.getProductId(), productLookupEntity.getTitle())
                    );
                }
            }
            return command;
        };
    }
}
