package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.ShippingDTO;
import com.parfenov.purdue_final.entity.Order;
import com.parfenov.purdue_final.entity.Shipping;
import com.parfenov.purdue_final.exception.NotFoundException;
import com.parfenov.purdue_final.mapper.ShippingMapper;
import com.parfenov.purdue_final.repository.OrderRepository;
import com.parfenov.purdue_final.repository.ShippingRepository;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShippingService {

  private final ShippingRepository shippingRepository;
  private final OrderRepository orderRepository;
  private final ShippingMapper shippingMapper;

  @Transactional
  public ShippingDTO createShipping(Long orderId, Double shippingCost, LocalDate estimatedDeliveryDate) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
    Shipping shipping = Shipping.builder()
        .order(order)
        .shippingCost(shippingCost)
        .estimatedDeliveryDate(estimatedDeliveryDate)
        .trackingNumber(generateTrackingNumber())
        .build();
    Shipping savedShipping = shippingRepository.save(shipping);
    return shippingMapper.toDto(savedShipping);
  }

  @Transactional(readOnly = true)
  public ShippingDTO getShippingByOrderId(Long orderId) {
    Shipping shipping = shippingRepository.findByOrderId(orderId)
        .orElseThrow(() -> new NotFoundException("Shipping information not found for order id: " + orderId));
    return shippingMapper.toDto(shipping);
  }

  @Transactional
  public void updateTrackingNumber(Long orderId, String trackingNumber) {
    Shipping shipping = shippingRepository.findByOrderId(orderId)
        .orElseThrow(() -> new NotFoundException("Shipping information not found for order id: " + orderId));
    shipping.setTrackingNumber(trackingNumber);
    shippingRepository.save(shipping);
  }

  private String generateTrackingNumber() {
    return "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  @Transactional
  public Shipping createShippingEntity(Order order, Double shippingCost, LocalDate estimatedDeliveryDate) {
    Shipping shipping = Shipping.builder()
        .order(order)
        .shippingCost(shippingCost)
        .estimatedDeliveryDate(estimatedDeliveryDate)
        .trackingNumber(UUID.randomUUID().toString())
        .build();

    return shippingRepository.save(shipping);
  }

}
