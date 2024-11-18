package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.ProductDTO;
import com.parfenov.purdue_final.entity.Product;
import com.parfenov.purdue_final.mapper.BaseMapper;
import com.parfenov.purdue_final.mapper.ProductMapper;
import com.parfenov.purdue_final.repository.BaseRepository;
import com.parfenov.purdue_final.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends BaseServiceImpl<Product, ProductDTO> implements BaseService<ProductDTO>{
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  public ProductService(
      BaseRepository<Product> baseRepository,
      BaseMapper<Product, ProductDTO> baseMapper,
      ProductRepository productRepository,
      ProductMapper productMapper) {
    super(baseRepository, Product.class, baseMapper);
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }
}
