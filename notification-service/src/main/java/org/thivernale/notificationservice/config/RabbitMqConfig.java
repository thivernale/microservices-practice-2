package org.thivernale.notificationservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thivernale.notificationservice.ApplicationProperties;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {
    private final ApplicationProperties applicationProperties;

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(applicationProperties.orderEventsExchange());
    }

    @Bean
    public Queue newOrdersQueue() {
        return new Queue(applicationProperties.newOrdersQueue());
    }

    @Bean
    public Binding newOrdersQueueBinding(Queue newOrdersQueue, DirectExchange exchange) {
        return BindingBuilder.bind(newOrdersQueue)
            .to(exchange)
            .with(applicationProperties.newOrdersQueue());
    }

    @Bean
    public Queue deliveredOrdersQueue() {
        return new Queue(applicationProperties.deliveredOrdersQueue());
    }

    @Bean
    public Binding deliveredOrdersQueueBinding(Queue deliveredOrdersQueue, DirectExchange exchange) {
        return BindingBuilder.bind(deliveredOrdersQueue)
            .to(exchange)
            .with(applicationProperties.deliveredOrdersQueue());
    }

    @Bean
    public Queue cancelledOrdersQueue() {
        return new Queue(applicationProperties.cancelledOrdersQueue());
    }

    @Bean
    public Binding cancelledOrdersQueueBinding(Queue cancelledOrdersQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cancelledOrdersQueue)
            .to(exchange)
            .with(applicationProperties.cancelledOrdersQueue());
    }

    @Bean
    public Queue errorOrdersQueue() {
        return new Queue(applicationProperties.errorOrdersQueue());
    }

    @Bean
    public Binding errorOrdersQueueBinding(Queue errorOrdersQueue, DirectExchange exchange) {
        return BindingBuilder.bind(errorOrdersQueue)
            .to(exchange)
            .with(errorOrdersQueue.getName());
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
