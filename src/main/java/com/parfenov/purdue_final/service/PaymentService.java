package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.CustomerDTO;
import com.parfenov.purdue_final.dto.PaymentDTO;
import com.parfenov.purdue_final.entity.Order;
import com.parfenov.purdue_final.entity.Payment;
import com.parfenov.purdue_final.enums.OrderStatus;
import com.parfenov.purdue_final.enums.PaymentStatus;
import com.parfenov.purdue_final.exception.NotFoundException;
import com.parfenov.purdue_final.mapper.PaymentMapper;
import com.parfenov.purdue_final.repository.OrderRepository;
import com.parfenov.purdue_final.repository.PaymentRepository;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;
  private final PaymentMapper paymentMapper;
  private final CustomerService customerService;

  @Transactional(readOnly = true)
  public PaymentDTO getPaymentByOrderId(Long orderId) {
    Payment payment = findPaymentByOrderId(orderId);
    return paymentMapper.toDto(payment);
  }

  @Transactional
  public Payment createPaymentEntity(Order order, Double amount, String paymentStatus) {
    validateOrderAndAmount(order, amount);
    Payment payment = buildNewPayment(order, amount, paymentStatus);
    return paymentRepository.save(payment);
  }

  @Transactional
  public PaymentDTO processPaymentByOrderId(Long orderId) {
    CustomerDTO currentCustomer = customerService.getCurrentCustomer();
    Order order = validateOrderOwnership(orderId, currentCustomer.getId());
    Payment payment = findPaymentByOrderId(orderId);
    validatePendingPayment(payment);
    boolean isPaymentSuccessful = simulateThirdPartyPayment();
    updatePaymentAndOrderStatuses(payment, order, isPaymentSuccessful);
    return paymentMapper.toDto(paymentRepository.save(payment));
  }

  @Transactional
  public void cancelPayment(Long paymentId) {
    CustomerDTO currentCustomer = customerService.getCurrentCustomer();
    Payment payment = validatePaymentOwnership(paymentId, currentCustomer.getId());
    updatePaymentStatus(payment, PaymentStatus.CANCELED.name());
  }

  @Transactional
  public PaymentDTO updatePaymentStatus(Long paymentId, String newStatus) {
    Payment payment = findPaymentById(paymentId);
    validatePaymentStatus(newStatus);
    updatePaymentStatus(payment, newStatus);
    if (PaymentStatus.COMPLETED.name().equals(newStatus)) {
      payment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
    }
    return paymentMapper.toDto(paymentRepository.save(payment));
  }

  private Payment findPaymentByOrderId(Long orderId) {
    return paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new NotFoundException("Payment not found for order id: " + orderId));
  }

  private Payment findPaymentById(Long paymentId) {
    return paymentRepository.findById(paymentId)
        .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));
  }

  private Order validateOrderOwnership(Long orderId, Long customerId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
    if (!order.getCustomer().getId().equals(customerId)) {
      throw new IllegalStateException("You can only process payments for your own orders.");
    }
    return order;
  }

  private Payment validatePaymentOwnership(Long paymentId, Long customerId) {
    Payment payment = findPaymentById(paymentId);
    Order order = payment.getOrder();
    if (!order.getCustomer().getId().equals(customerId)) {
      throw new IllegalStateException("You can only cancel payments for your own orders.");
    }
    return payment;
  }

  private void validateOrderAndAmount(Order order, Double amount) {
    if (order == null || amount == null) {
      throw new IllegalArgumentException("Order and amount must not be null.");
    }
  }

  private void validatePendingPayment(Payment payment) {
    if (!PaymentStatus.PENDING.name().equals(payment.getPaymentStatus())) {
      throw new IllegalStateException("Only payments with status 'PENDING' can be processed.");
    }
  }

  private void validatePaymentStatus(String status) {
    if (!isValidPaymentStatus(status)) {
      throw new IllegalArgumentException("Invalid payment status: " + status);
    }
  }

  private boolean isValidPaymentStatus(String status) {
    try {
      PaymentStatus.valueOf(status);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  private Payment buildNewPayment(Order order, Double amount, String paymentStatus) {
    return Payment.builder()
        .order(order)
        .amount(amount)
        .paymentStatus(paymentStatus)
        .paymentDate(new Timestamp(System.currentTimeMillis()))
        .build();
  }

  private void updatePaymentAndOrderStatuses(Payment payment, Order order, boolean isPaymentSuccessful) {
    if (isPaymentSuccessful) {
      payment.setPaymentStatus(PaymentStatus.COMPLETED.name());
      payment.setPaymentDate(new Timestamp(System.currentTimeMillis()));
      order.setStatus(OrderStatus.NOT_SHIPPED.name());
      orderRepository.save(order);
    } else {
      payment.setPaymentStatus(PaymentStatus.FAILED.name());
    }
  }

  private void updatePaymentStatus(Payment payment, String newStatus) {
    payment.setPaymentStatus(newStatus);
  }

  private boolean simulateThirdPartyPayment() {
    return Math.random() < 0.8;
  }
}
