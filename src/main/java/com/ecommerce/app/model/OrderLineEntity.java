package com.ecommerce.app.model;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table
@Entity(name = "ORDER_LINE")
public class OrderLineEntity extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ORDER_SEQ")
    @SequenceGenerator(name = "ORDER_SEQ", sequenceName = "ORDER_SEQ", allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
    private Long productId;
    private Long quantity;
}
