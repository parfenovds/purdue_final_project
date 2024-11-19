package com.parfenov.purdue_final.mapper;

import com.parfenov.purdue_final.dto.OrderProductDTO;
import com.parfenov.purdue_final.entity.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderProductMapper extends BaseMapper<OrderProduct, OrderProductDTO> {

  @Override
  @Mapping(source = "product.id", target = "productId")
  @Mapping(source = "product.name", target = "productName")
  @Mapping(source = "unitPrice", target = "unitPrice")
  @Mapping(target = "totalCost", expression = "java(orderProduct.getUnitPrice() * orderProduct.getQuantity())")
  OrderProductDTO toDto(OrderProduct orderProduct);

  @Override
  OrderProduct toEntity(OrderProductDTO orderProductDTO);
}
