package com.parfenov.purdue_final.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingDTO implements BaseDTO {
  private Double shippingCost;
  private LocalDate estimatedDeliveryDate;
  private String trackingNumber;
}
