package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.ProductDTO;
import com.parfenov.purdue_final.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductDTO>{
}
