package com.parfenov.purdue_final.repository;

import com.parfenov.purdue_final.entity.OrderProduct;
import com.parfenov.purdue_final.entity.OrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductId> {

}
