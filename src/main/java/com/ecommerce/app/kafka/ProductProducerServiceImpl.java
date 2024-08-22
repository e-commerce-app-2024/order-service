package com.ecommerce.app.kafka;

import com.ecommerce.app.enums.KafkaTopicName;
import com.ecommerce.app.integration.product.model.PurchaseResponse;
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
public class ProductProducerServiceImpl implements ProductProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void rollbackPurchaseProduct(PurchaseResponse purchaseResponse) {
        log.info("start rollback purchase product process for : <{}>", purchaseResponse);
        try {
            Message message = MessageBuilder
                    .withPayload(purchaseResponse)
                    .setHeader(KafkaHeaders.TOPIC, KafkaTopicName.ROLLBACK_PURCHASE).build();
            kafkaTemplate.send(message);
        } catch (Exception ex) {
            log.error(ex);
        }
    }

}
