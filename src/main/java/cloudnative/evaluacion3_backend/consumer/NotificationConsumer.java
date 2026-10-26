package cloudnative.evaluacion3_backend.consumer;


import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.queue.notification}", ackMode = "MANUAL")
    public void processNotification(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.info("Procesando notificación recibida: {}", message);

            // Simulación de validación de negocio
            if (message.contains("ERROR")) {
                throw new IllegalArgumentException("El contenido del mensaje es inválido.");
            }

            // Confirmación explícita de éxito (ACK)
            channel.basicAck(tag, false);
            log.info("Notificación procesada exitosamente (ACK enviado).");

        } catch (Exception e) {
            log.error("Error al procesar la notificación: {}. Enviando a DLQ...", e.getMessage());
            
            // Recharzamos el mensaje con requeue = false para redirigirlo a la DLQ configurada (NACK)
            channel.basicNack(tag, false, false);
        }
    }
}