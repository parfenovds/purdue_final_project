package com.parfenov.purdue_final.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDTO {
  private Long id;
  private Long customerId;
  private List<ShoppingCartProductDTO> products;
  private Double shippingCost;
  private Double totalCost;
  private Double taxAmount;
}
