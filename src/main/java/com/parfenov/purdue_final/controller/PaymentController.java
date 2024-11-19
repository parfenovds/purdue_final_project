package com.parfenov.purdue_final.controller;

import com.parfenov.purdue_final.dto.PaymentDTO;
import com.parfenov.purdue_final.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Endpoints for managing payments")
public class PaymentController {

  private final PaymentService paymentService;

  @Operation(summary = "Get payment details by order ID", description = "Retrieve payment details for a specific order.")
  @GetMapping("/{orderId}")
  public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable @Parameter(description = "ID of the order") Long orderId) {
    PaymentDTO payment = paymentService.getPaymentByOrderId(orderId);
    return ResponseEntity.ok(payment);
  }

  @Operation(summary = "Process payment", description = "Process payment for a specific order.")
  @PostMapping("/{orderId}/process")
  public ResponseEntity<PaymentDTO> processPaymentByOrderId(@PathVariable @Parameter(description = "ID of the order") Long orderId) {
    PaymentDTO processedPayment = paymentService.processPaymentByOrderId(orderId);
    return ResponseEntity.ok(processedPayment);
  }

  @Operation(summary = "Cancel payment", description = "Cancel a payment by its ID.")
  @PostMapping("/{paymentId}/cancel")
  public ResponseEntity<Void> cancelPayment(@PathVariable @Parameter(description = "ID of the payment") Long paymentId) {
    paymentService.cancelPayment(paymentId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update payment status", description = "Update the status of a payment (ADMIN only).")
  @PatchMapping("/{paymentId}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PaymentDTO> updatePaymentStatus(
      @PathVariable @Parameter(description = "ID of the payment") Long paymentId,
      @RequestParam @Parameter(description = "New status for the payment") String newStatus
  ) {
    PaymentDTO updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus);
    return ResponseEntity.ok(updatedPayment);
  }
}
