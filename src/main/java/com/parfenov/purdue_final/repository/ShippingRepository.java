package com.parfenov.purdue_final.repository;

import com.parfenov.purdue_final.entity.Shipping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, Long> {
  Optional<Shipping> findByOrderId(Long orderId);
}
