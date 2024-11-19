package com.parfenov.purdue_final.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO implements BaseDTO {
  private Long id;
  private Double amount;
  private String paymentStatus;
  private Timestamp paymentDate;
}
