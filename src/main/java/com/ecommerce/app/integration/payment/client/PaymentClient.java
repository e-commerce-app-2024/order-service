package com.ecommerce.app.integration.payment.client;


import com.ecommerce.app.integration.payment.model.PaymentRequest;
import com.ecommerce.app.integration.payment.model.PaymentResponse;
import com.ecommerce.app.payload.AppResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${spring.config.payment-url}"
)
public interface PaymentClient {

    @PostMapping
    AppResponse<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request);
}
