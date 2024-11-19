package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.ShippingDTO;
import com.parfenov.purdue_final.entity.Shipping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingMapper extends BaseMapper<Shipping, ShippingDTO> {

  @Override
  @Mapping(source = "shippingCost", target = "shippingCost")
  @Mapping(source = "estimatedDeliveryDate", target = "estimatedDeliveryDate")
  @Mapping(source = "trackingNumber", target = "trackingNumber")
  ShippingDTO toDto(Shipping shipping);

  @Override
  Shipping toEntity(ShippingDTO shippingDTO);
}
