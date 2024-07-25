package com.ecommerce.app.model;


import com.ecommerce.app.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table
@Entity(name = "CUSTOMER_ORDERS")
public class OrderEntity extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORDER_SEQ")
    @SequenceGenerator(name = "ORDER_SEQ", sequenceName = "ORDER_SEQ", allocationSize = 1)
    private Long id;
    private String customerId;
    private String reference;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @OneToMany(mappedBy = "order")
    private List<OrderLineEntity> orderLines;
}
