package com.ecommerce.app.exception;

public class PaymentFailureException extends RuntimeException {
    public PaymentFailureException(String msg) {
        super(msg);
    }
}
