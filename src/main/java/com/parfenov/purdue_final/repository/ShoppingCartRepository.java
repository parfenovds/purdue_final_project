package com.parfenov.purdue_final.repository;

import com.parfenov.purdue_final.entity.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
  Optional<ShoppingCart> findByCustomerId(Long customerId);
}
