package com.ecommerce.app.repo;


import com.ecommerce.app.dto.ProductCount;
import com.ecommerce.app.model.OrderLineEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface OrderLineRepo extends BaseRepo<OrderLineEntity> {

    Page<OrderLineEntity> findByOrderId(Long orderId, Pageable pageable);

    @Query(value = """
            select new com.ecommerce.app.dto.ProductCount(ol.productId ,count(ol.quantity))
            from ORDER_LINE ol
            group by ol.productId
            """)
    List<ProductCount> getProductsCount();

    @Query(value = """
            select new com.ecommerce.app.dto.ProductCount(ol.productId ,count(ol.quantity))
            from ORDER_LINE ol
            where ol.createdAt between :from and :to
            group by ol.productId
            """)
    List<ProductCount> getProductsCount(@Param("from") Date from, @Param("to") Date to);

}
