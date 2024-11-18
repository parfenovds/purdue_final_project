package com.parfenov.purdue_final.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartProductDTO {
  private Long productId;
  private String productName;
  private Integer quantity;
  private Double price;
  private Double totalCost;
}