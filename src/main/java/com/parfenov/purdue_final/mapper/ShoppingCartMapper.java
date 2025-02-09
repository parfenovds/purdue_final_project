package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.ShoppingCartDTO;
import com.parfenov.purdue_final.entity.ShoppingCart;
import com.parfenov.purdue_final.entity.ShoppingCartProduct;
import com.parfenov.purdue_final.service.CostCalculator;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {ShoppingCartProductMapper.class})
public abstract class ShoppingCartMapper {

  @Autowired
  private CostCalculator costCalculator;

  @Mapping(target = "taxAmount", ignore = true)
  @Mapping(target = "shippingCost", ignore = true)
  @Mapping(target = "totalCost", ignore = true)
  @Mapping(target = "customerId", source = "customer.id")
  public abstract ShoppingCartDTO toDto(ShoppingCart entity);

  @AfterMapping
  protected void calculateAdditionalFields(@MappingTarget ShoppingCartDTO dto, ShoppingCart entity) {
    double totalItemsCost = calculateTotalItemsCost(entity.getProducts());
    dto.setTaxAmount(costCalculator.calculateTax(totalItemsCost));
    dto.setShippingCost(costCalculator.calculateShippingCost());
    dto.setTotalCost(costCalculator.calculateTotalCost(totalItemsCost));
  }

  protected double calculateTotalItemsCost(List<ShoppingCartProduct> products) {
    return products.stream()
        .mapToDouble(p -> p.getProduct().getPrice() * p.getQuantity())
        .sum();
  }
}
