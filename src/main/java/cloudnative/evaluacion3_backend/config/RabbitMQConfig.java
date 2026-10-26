package cloudnative.evaluacion3_backend.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Inyectamos los nombres centralizados desde application.yml
    @Value("${app.rabbitmq.exchange.notifications}")
    private String exchangeName;

    @Value("${app.rabbitmq.queue.notification}")
    private String queueName;

    @Value("${app.rabbitmq.queue.dlq}")
    private String dlqName;

    @Value("${app.rabbitmq.routing-key.notification}")
    private String routingKey;

    @Value("${app.rabbitmq.routing-key.dlq}")
    private String dlqRoutingKey;

    // 1. Declaración del Topic Exchange principal
    @Bean
    public TopicExchange mainExchange() {
        return new TopicExchange(exchangeName);
    }

    // 2. Declaración de la Dead Letter Queue (DLQ)
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlqName).build();
    }

    // 3. Declaración de la Cola Principal configurada con su DLQ
    @Bean
    public Queue mainQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", dlqRoutingKey)
                .build();
    }

    // 4. Binding entre la Cola Principal y el Exchange
    @Bean
    public Binding mainBinding(Queue mainQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(mainQueue).to(mainExchange).with(routingKey);
    }

    // 5. Binding entre la DLQ y el Exchange
    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, TopicExchange mainExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(mainExchange).with(dlqRoutingKey);
    }
}