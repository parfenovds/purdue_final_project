package com.parfenov.purdue_final.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO implements BaseDTO {
  private Long id;
  private String name;
  private String description;
  private Double price;
  private Integer stockQuantity;
}
