package com.parfenov.purdue_final.repository;

import com.parfenov.purdue_final.entity.ShoppingCartProduct;
import com.parfenov.purdue_final.entity.ShoppingCartProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartProductRepository extends JpaRepository<ShoppingCartProduct, ShoppingCartProductId> {
}
