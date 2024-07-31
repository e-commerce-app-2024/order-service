package com.ecommerce.app.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentFailureException extends RuntimeException {

    private String requestId;

    public PaymentFailureException(String msg) {
        super(msg);
    }

    public PaymentFailureException(String message, String requestId) {
        super(message);
        this.requestId = requestId;
    }
}
