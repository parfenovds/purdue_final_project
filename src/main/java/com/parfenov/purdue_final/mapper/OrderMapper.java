package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.OrderDTO;
import com.parfenov.purdue_final.entity.Order;
import com.parfenov.purdue_final.entity.OrderProduct;
import com.parfenov.purdue_final.entity.ShoppingCart;
import com.parfenov.purdue_final.entity.ShoppingCartProduct;
import com.parfenov.purdue_final.service.CostCalculator;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {OrderProductMapper.class, ShippingMapper.class, PaymentMapper.class})
public abstract class OrderMapper {

  @Autowired
  private CostCalculator costCalculator;

  @Mapping(target = "taxAmount", ignore = true)
  @Mapping(target = "shippingCost", ignore = true)
  @Mapping(target = "finalAmount", ignore = true)
  @Mapping(target = "totalAmount", ignore = true)
  @Mapping(target = "customerId", source = "customer.id")
  public abstract OrderDTO toDto(Order entity);

  public abstract List<OrderDTO> toDtoList(List<Order> entities);

  @AfterMapping
  protected void calculateAdditionalFields(@MappingTarget OrderDTO dto, Order entity) {
    double totalItemsCost = calculateTotalItemsCost(entity.getProducts());
    dto.setTotalAmount(totalItemsCost);
    dto.setTaxAmount(costCalculator.calculateTax(totalItemsCost));
    dto.setShippingCost(costCalculator.calculateShippingCost());
    dto.setFinalAmount(costCalculator.calculateTotalCost(totalItemsCost));
  }

  protected double calculateTotalItemsCost(List<OrderProduct> products) {
    return products.stream()
        .mapToDouble(p -> p.getUnitPrice() * p.getQuantity())
        .sum();
  }
}
