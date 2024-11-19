package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.PaymentDTO;
import com.parfenov.purdue_final.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends BaseMapper<Payment, PaymentDTO> {

  @Override
  @Mapping(source = "id", target = "id")
  @Mapping(source = "amount", target = "amount")
  @Mapping(source = "paymentStatus", target = "paymentStatus")
  @Mapping(source = "paymentDate", target = "paymentDate")
  PaymentDTO toDto(Payment payment);

  @Override
  Payment toEntity(PaymentDTO paymentDTO);
}
