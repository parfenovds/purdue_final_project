package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.CustomerDTO;
import com.parfenov.purdue_final.entity.Customer;
import com.parfenov.purdue_final.exception.NotFoundException;
import com.parfenov.purdue_final.mapper.CustomerMapper;
import com.parfenov.purdue_final.repository.CustomerRepository;
import com.parfenov.purdue_final.security.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public CustomerDTO getCustomerById(Long customerId) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));
    return customerMapper.toDto(customer);
  }

  @Transactional(readOnly = true)
  public CustomerDTO getCurrentCustomer() {
    String currentUsername = getCurrentUsername();
    Customer customer = customerRepository.findByEmail(currentUsername)
        .orElseThrow(() -> new NotFoundException("Customer not found with email: " + currentUsername));
    return customerMapper.toDto(customer);
  }

  @Transactional
  public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
    if (customerRepository.existsByEmail(customerDTO.getEmail())) {
      throw new IllegalStateException("Customer with email " + customerDTO.getEmail() + " already exists.");
    }

    Customer customer = customerMapper.toEntity(customerDTO);
    customer.setPassword(passwordEncoder.encode(customerDTO.getPassword())); // Encode password
    customer.setRole(Role.ROLE_CUSTOMER);
    Customer savedCustomer = customerRepository.save(customer);

    return customerMapper.toDto(savedCustomer);
  }

  @Transactional
  public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) {
    Customer existingCustomer = customerRepository.findById(customerId)
        .orElseThrow(() -> new NotFoundException("Customer not found with id: " + customerId));

    existingCustomer.setLogin(customerDTO.getLogin());
    existingCustomer.setEmail(customerDTO.getEmail());
    if (customerDTO.getPassword() != null) {
      existingCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
    }

    Customer updatedCustomer = customerRepository.save(existingCustomer);
    return customerMapper.toDto(updatedCustomer);
  }

  @Transactional
  public void deleteCustomer(Long customerId) {
    if (!customerRepository.existsById(customerId)) {
      throw new NotFoundException("Customer not found with id: " + customerId);
    }
    customerRepository.deleteById(customerId);
  }

  private String getCurrentUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
