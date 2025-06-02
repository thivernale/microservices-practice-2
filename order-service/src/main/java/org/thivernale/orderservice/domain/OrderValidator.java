package org.thivernale.orderservice.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thivernale.orderservice.client.catalog.ProductDto;
import org.thivernale.orderservice.client.catalog.ProductServiceClient;
import org.thivernale.orderservice.domain.models.CreateOrderRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderValidator implements Validator {
    private final ProductServiceClient productServiceClient;

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateOrderRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof CreateOrderRequest request) {
            request.orderItems()
                .forEach(orderItem -> {
                    int idx = request.orderItems()
                        .stream()
                        .toList()
                        .indexOf(orderItem);

                    Optional<ProductDto> productDto = productServiceClient.findByCode(orderItem.code());

                    if (productDto.isEmpty()) {
                        errors.rejectValue("orderItems[%d].code".formatted(idx), null, "Product not found: [%d] %s".formatted(idx, orderItem.code()));
                    } else if (productDto.get()
                        .price()
                        .compareTo(orderItem.price()) != 0) {
                        errors.rejectValue("orderItems[%d].price".formatted(idx), null, "Product price is incorrect: [%d] %s".formatted(idx, orderItem.code()));
                    }
                });

            if (!errors.getAllErrors()
                .isEmpty()) {
                List<String> errorsMessages = errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
                log.info("{}", errorsMessages);

                throw new InvalidOrderException("Invalid product data for order");
            }
        }
    }

    public void validate(Object target) {
        validate(target, new BeanPropertyBindingResult(target, "createOrderRequest"));
    }
}
