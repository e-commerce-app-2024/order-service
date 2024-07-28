package com.ecommerce.app.repo;


import com.ecommerce.app.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepo extends BaseRepo<OrderEntity> {

    Page<OrderEntity> findByCustomerId(String customerId, Pageable pageable);
}
