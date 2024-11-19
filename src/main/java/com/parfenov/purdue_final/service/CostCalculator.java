package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.entity.OrderProduct;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CostCalculator {

  private static final double TAX_RATE = 0.1;
  private static final double SHIPPING_COST = 10.0;

  public double calculateTotalItemsCost(List<OrderProduct> products) {
    return products.stream()
        .mapToDouble(p -> p.getUnitPrice() * p.getQuantity())
        .sum();
  }

  public double calculateTax(double itemsCost) {
    return itemsCost * TAX_RATE;
  }

  public double calculateShippingCost() {
    return SHIPPING_COST;
  }

  public double calculateTotalCost(double itemsCost) {
    double tax = calculateTax(itemsCost);
    double shipping = calculateShippingCost();
    return itemsCost + tax + shipping;
  }
}
