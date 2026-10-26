package cloudnative.evaluacion3_backend.service;


import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;

@Service
public class RabbitAdminService {

    private final AmqpAdmin amqpAdmin;

    public RabbitAdminService(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    // Encapsula la lógica de creación de colas
    public void createQueue(String queueName, boolean durable) {
        Queue queue = new Queue(queueName, durable, false, false);
        amqpAdmin.declareQueue(queue);
    }

    // Encapsula la lógica de eliminación de colas
    public boolean deleteQueue(String queueName) {
        return amqpAdmin.deleteQueue(queueName);
    }
}