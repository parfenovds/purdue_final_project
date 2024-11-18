package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.ShoppingCartProductDTO;
import com.parfenov.purdue_final.entity.ShoppingCartProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ShoppingCartProductMapper extends BaseMapper<ShoppingCartProduct, ShoppingCartProductDTO> {
  @Override
  @Mapping(source = "product.id", target = "productId")
  @Mapping(source = "product.name", target = "productName")
  @Mapping(target = "price", source = "product.price")
  @Mapping(
      target = "totalCost",
      expression = "java((shoppingCartProduct.getProduct() != null " +
                   "&& shoppingCartProduct.getProduct().getPrice() != null " +
                   "&& shoppingCartProduct.getQuantity() != null) " +
                   "? shoppingCartProduct.getProduct().getPrice() * shoppingCartProduct.getQuantity() : 0.0)"
  )
  ShoppingCartProductDTO toDto(ShoppingCartProduct shoppingCartProduct);

  @Override
  ShoppingCartProduct toEntity(ShoppingCartProductDTO shoppingCartProductDTO);
}
