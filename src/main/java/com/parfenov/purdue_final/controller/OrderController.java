package com.parfenov.purdue_final.controller;

import com.parfenov.purdue_final.dto.OrderDTO;
import com.parfenov.purdue_final.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Orders", description = "Endpoints for managing orders")
public class OrderController {

  private final OrderService orderService;

  @Operation(summary = "Get an order by ID", description = "Retrieve details of a specific order by its ID.")
  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable @Parameter(description = "ID of the order") Long orderId) {
    OrderDTO order = orderService.getOrderById(orderId);
    return ResponseEntity.ok(order);
  }

  @Operation(summary = "Get all customer orders", description = "Retrieve all orders for the logged-in customer.")
  @GetMapping
  public ResponseEntity<List<OrderDTO>> getCustomerOrders() {
    List<OrderDTO> orders = orderService.getCustomerOrders();
    return ResponseEntity.ok(orders);
  }

  @Operation(summary = "Create an order", description = "Create a new order from the shopping cart.")
  @PostMapping
  public ResponseEntity<OrderDTO> createOrder() {
    OrderDTO order = orderService.createOrder();
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
  }

  @Operation(summary = "Cancel an order", description = "Cancel an order by its ID if it has not been shipped.")
  @PatchMapping("/{orderId}/cancel")
  public ResponseEntity<OrderDTO> cancelOrder(@PathVariable @Parameter(description = "ID of the order") Long orderId) {
    OrderDTO canceledOrder = orderService.cancelOrder(orderId);
    return ResponseEntity.ok(canceledOrder);
  }

  @Operation(summary = "Update order status", description = "Update the status of an order (ADMIN only).")
  @PatchMapping("/{orderId}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<OrderDTO> updateOrderStatus(
      @PathVariable @Parameter(description = "ID of the order") Long orderId,
      @RequestParam @Parameter(description = "New status for the order") String newStatus
  ) {
    OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
    return ResponseEntity.ok(updatedOrder);
  }
}
