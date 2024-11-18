package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.ShoppingCartDTO;
import com.parfenov.purdue_final.entity.ShoppingCart;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ShoppingCartProductMapper.class})
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart, ShoppingCartDTO> {

  @Mapping(target = "taxAmount", expression = "java(calculateTax(entity))")
  @Mapping(target = "shippingCost", expression = "java(calculateShippingCost(entity))")
  @Mapping(target = "totalCost", expression = "java(calculateTotalCost(entity))")
  @Mapping(target = "customerId", source = "customer.id")
  ShoppingCartDTO toDto(ShoppingCart entity);

  default Double calculateTotalItemsCost(ShoppingCart cart) {
    return cart.getProducts().stream()
        .mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity())
        .sum();
  }

  default Double calculateTax(ShoppingCart cart) {
    double taxRate = 0.1;
    return calculateTotalItemsCost(cart) * taxRate;
  }

  default Double calculateShippingCost(ShoppingCart cart) {
    return 10.0;
  }

  default Double calculateTotalCost(ShoppingCart cart) {
    return calculateTotalItemsCost(cart) + calculateTax(cart) + calculateShippingCost(cart);
  }
}