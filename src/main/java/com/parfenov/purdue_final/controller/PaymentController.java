package com.parfenov.purdue_final.controller;

import com.parfenov.purdue_final.dto.PaymentDTO;
import com.parfenov.purdue_final.service.PaymentService;
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
public class PaymentController {

  private final PaymentService paymentService;

  @GetMapping("/{orderId}")
  public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId) {
    PaymentDTO payment = paymentService.getPaymentByOrderId(orderId);
    return ResponseEntity.ok(payment);
  }

  @PostMapping("/{orderId}/process")
  public ResponseEntity<PaymentDTO> processPaymentByOrderId(@PathVariable Long orderId) {
    PaymentDTO processedPayment = paymentService.processPaymentByOrderId(orderId);
    return ResponseEntity.ok(processedPayment);
  }

  @PostMapping("/{paymentId}/cancel")
  public ResponseEntity<Void> cancelPayment(@PathVariable Long paymentId) {
    paymentService.cancelPayment(paymentId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{paymentId}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PaymentDTO> updatePaymentStatus(
      @PathVariable Long paymentId,
      @RequestParam String newStatus
  ) {
    PaymentDTO updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus);
    return ResponseEntity.ok(updatedPayment);
  }
}
