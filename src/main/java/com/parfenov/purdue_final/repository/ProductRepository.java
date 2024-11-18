package com.parfenov.purdue_final.repository;

import com.parfenov.purdue_final.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends BaseRepository<Product> {
}
