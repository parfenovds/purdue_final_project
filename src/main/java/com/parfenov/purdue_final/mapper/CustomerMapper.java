package com.parfenov.purdue_final.mapper;


import com.parfenov.purdue_final.dto.CustomerDTO;
import com.parfenov.purdue_final.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends BaseMapper<Customer, CustomerDTO> {
  @Override
  CustomerDTO toDto(Customer customer);

  @Override
  Customer toEntity(CustomerDTO customerDTO);
}
