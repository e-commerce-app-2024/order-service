package com.ecommerce.app.repo;


import com.ecommerce.app.model.OrderLineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderLineRepo extends BaseRepo<OrderLineEntity> {

    Page<OrderLineEntity> findByOrderId(Long orderId, Pageable pageable);

}
