package com.parfenov.purdue_final.controller;

import com.parfenov.purdue_final.dto.OrderDTO;
import com.parfenov.purdue_final.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
    OrderDTO order = orderService.getOrderById(orderId);
    return ResponseEntity.ok(order);
  }

  @GetMapping
  public ResponseEntity<List<OrderDTO>> getCustomerOrders() {
    List<OrderDTO> orders = orderService.getCustomerOrders();
    return ResponseEntity.ok(orders);
  }

  @PostMapping
  public ResponseEntity<OrderDTO> createOrder() {
    OrderDTO order = orderService.createOrder();
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @PatchMapping("/{orderId}/cancel")
  public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId) {
    OrderDTO canceledOrder = orderService.cancelOrder(orderId);
    return ResponseEntity.ok(canceledOrder);
  }

  @PatchMapping("/{orderId}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderDTO> updateOrderStatus(
      @PathVariable Long orderId,
      @RequestParam String newStatus
  ) {
    OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
    return ResponseEntity.ok(updatedOrder);
  }
}

