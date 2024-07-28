package com.ecommerce.app.exception;

public class OrderLineNotFoundException extends RuntimeException {
    public OrderLineNotFoundException(Long id) {
        super("Order line with id " + id + " not found");
    }
}
