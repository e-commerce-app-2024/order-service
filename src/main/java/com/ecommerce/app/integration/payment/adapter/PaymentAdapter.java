package com.ecommerce.app.integration.payment.adapter;


import com.ecommerce.app.exception.CustomerIntegrationException;
import com.ecommerce.app.integration.payment.client.PaymentClient;
import com.ecommerce.app.integration.payment.model.PaymentRequest;
import com.ecommerce.app.integration.payment.model.PaymentResponse;
import com.ecommerce.app.payload.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentAdapter {

    private final PaymentClient paymentClient;

    public boolean doPayment(PaymentRequest request) {
        AppResponse<PaymentResponse> appResponse = paymentClient.createPayment(request);
        if (appResponse.success()) {
            return appResponse.payload().success();
        } else {
            List<String> errors = appResponse.error().errors();
            throw new CustomerIntegrationException(errors.get(0));
        }
    }
}
