package com.parfenov.purdue_final.security.service;

import com.parfenov.purdue_final.entity.Customer;
import com.parfenov.purdue_final.exception.NotFoundException;
import com.parfenov.purdue_final.repository.CustomerRepository;
import com.parfenov.purdue_final.security.AuthenticationRequest;
import com.parfenov.purdue_final.security.AuthenticationResponse;
import com.parfenov.purdue_final.security.RegisterRequest;
import com.parfenov.purdue_final.security.enums.Role;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    Customer customer = Customer.builder()
        .login(request.getLogin())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.ROLE_CUSTOMER) // Установка роли
        .build();
    customerRepository.save(customer);
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("role", customer.getRole());
    String jwtToken = jwtService.generateToken(extraClaims, customer);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    Customer customer = customerRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new NotFoundException(
                Customer.class,
                request.getEmail()
            )
        );
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("role", customer.getRole());
    String jwtToken = jwtService.generateToken(extraClaims, customer);
    return AuthenticationResponse.builder().token(jwtToken).build();
  }
}
