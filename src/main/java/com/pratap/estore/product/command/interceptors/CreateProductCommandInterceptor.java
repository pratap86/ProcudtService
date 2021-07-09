package com.pratap.estore.product.command.interceptors;

import com.pratap.estore.product.command.CreateProductCommand;
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

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            log.info("Interceptor called with Intercepted command={}", command.getPayload());
            if (CreateProductCommand.class.equals(command.getPayloadType())){

                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
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
            return command;
        };
    }
}
