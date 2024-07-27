package com.ecommerce.app.integration.customer.model;


public record Address(
        String street,
        String houseNumber,
        String zipCode
) {
}
