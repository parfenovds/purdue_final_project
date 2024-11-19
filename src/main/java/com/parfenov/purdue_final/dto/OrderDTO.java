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
public class OrderDTO implements BaseDTO {
  private Long id;
  private Long customerId;
  private List<OrderProductDTO> products;
  private String status;
  private Double totalAmount;
  private Double shippingCost;
  private Double taxAmount;
  private Double finalAmount;
  private ShippingDTO shipping;
  private PaymentDTO payment;
}