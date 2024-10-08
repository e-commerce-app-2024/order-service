package com.ecommerce.app.kafka;

import com.ecommerce.app.dto.OrderConfirmation;
import com.ecommerce.app.enums.KafkaTopicName;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderProducerServiceImpl implements OrderProducerService {

    private final KafkaTemplate<String, OrderConfirmation> kafkaTemplate;

    @Override
    public void sendOrderConfirmation(OrderConfirmation orderConfirmation) {
        log.info("send order confirmation with body: <{}>", orderConfirmation);
        try {
            Message message = MessageBuilder
                    .withPayload(orderConfirmation)
                    .setHeader(KafkaHeaders.TOPIC, KafkaTopicName.ORDER).build();
            kafkaTemplate.send(message);
        } catch (Exception ex) {
            log.error(ex);
        }
    }
}
