package com.ecommerce.app.enums;

public interface KafkaTopicName {
    String ORDER = "order";
    String ROLLBACK_PURCHASE = "rollback_purchase";
    String DELETE_PURCHASE = "delete_purchase_log";
}
