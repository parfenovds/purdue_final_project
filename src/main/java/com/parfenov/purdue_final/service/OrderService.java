package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.CustomerDTO;
import com.parfenov.purdue_final.dto.OrderDTO;
import com.parfenov.purdue_final.dto.PaymentDTO;
import com.parfenov.purdue_final.dto.ShippingDTO;
import com.parfenov.purdue_final.dto.ShoppingCartDTO;
import com.parfenov.purdue_final.dto.ShoppingCartProductDTO;
import com.parfenov.purdue_final.entity.Customer;
import com.parfenov.purdue_final.entity.Order;
import com.parfenov.purdue_final.entity.OrderProduct;
import com.parfenov.purdue_final.entity.Payment;
import com.parfenov.purdue_final.entity.Product;
import com.parfenov.purdue_final.entity.Shipping;
import com.parfenov.purdue_final.enums.OrderStatus;
import com.parfenov.purdue_final.enums.PaymentStatus;
import com.parfenov.purdue_final.exception.NotFoundException;
import com.parfenov.purdue_final.mapper.CustomerMapper;
import com.parfenov.purdue_final.mapper.OrderMapper;
import com.parfenov.purdue_final.mapper.PaymentMapper;
import com.parfenov.purdue_final.mapper.ShippingMapper;
import com.parfenov.purdue_final.repository.CustomerRepository;
import com.parfenov.purdue_final.repository.OrderRepository;
import com.parfenov.purdue_final.repository.ProductRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ShoppingCartService shoppingCartService;
  private final ProductRepository productRepository;
  private final PaymentService paymentService;
  private final ShippingService shippingService;
  private final CustomerService customerService;
  private final OrderMapper orderMapper;
  private final CustomerMapper customerMapper;
  private final CostCalculator costCalculator;

  @Transactional(readOnly = true)
  public OrderDTO getOrderById(Long orderId) {
    Order order = fetchOrderById(orderId);
    return orderMapper.toDto(order);
  }

  @Transactional(readOnly = true)
  public List<OrderDTO> getCustomerOrders() {
    CustomerDTO currentCustomer = customerService.getCurrentCustomer();
    List<Order> orders = orderRepository.findByCustomerId(currentCustomer.getId());
    return orderMapper.toDtoList(orders);
  }

  @Transactional
  public OrderDTO createOrder() {
    CustomerDTO currentCustomer = customerService.getCurrentCustomer();
    ShoppingCartDTO shoppingCart = validateShoppingCart();
    Order order = initializeOrder(currentCustomer);
    transferCartProductsToOrder(shoppingCart, order);
    Order savedOrder = orderRepository.save(order);
    double itemsCost = costCalculator.calculateTotalItemsCost(savedOrder.getProducts());
    double tax = costCalculator.calculateTax(itemsCost);
    double shipping = costCalculator.calculateShippingCost();
    double totalCost = costCalculator.calculateTotalCost(itemsCost);
    linkPaymentAndShipping(savedOrder, totalCost, shipping);
    Order finalOrder = orderRepository.save(savedOrder);
    shoppingCartService.clearShoppingCart(currentCustomer.getId());
    return orderMapper.toDto(finalOrder);
  }

  @Transactional
  public OrderDTO cancelOrder(Long orderId) {
    Order order = fetchOrderById(orderId);
    validateOrderCancelable(order);

    returnProductsToStock(order);

    if (order.getPayment() != null) {
      paymentService.cancelPayment(order.getPayment().getId());
    }

    order.setStatus(OrderStatus.CANCELED.name());
    return orderMapper.toDto(orderRepository.save(order));
  }

  @Transactional
  public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
    Order order = fetchOrderById(orderId);
    validateOrderStatusForUpdate(order);

    order.setStatus(newStatus);
    return orderMapper.toDto(orderRepository.save(order));
  }

  private Order fetchOrderById(Long orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
  }

  private ShoppingCartDTO validateShoppingCart() {
    ShoppingCartDTO shoppingCart = shoppingCartService.getProductsInCart();
    if (shoppingCart.getProducts().isEmpty()) {
      throw new IllegalStateException("Shopping cart is empty, cannot create order.");
    }
    return shoppingCart;
  }

  private Order initializeOrder(CustomerDTO currentCustomer) {
    Order order = new Order();
    order.setCustomer(customerMapper.toEntity(customerService.getCustomerById(currentCustomer.getId())));
    order.setStatus(OrderStatus.WAIT_FOR_PAYMENT.name());
    return order;
  }

  private void transferCartProductsToOrder(ShoppingCartDTO shoppingCart, Order order) {
    shoppingCart.getProducts().forEach(cartProduct -> {
      Product product = fetchProductById(cartProduct.getProductId());
      validateProductStock(cartProduct, product);
      adjustProductStock(cartProduct, product);
      addProductToOrder(order, cartProduct, product);
    });
  }

  private Product fetchProductById(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
  }

  private void validateProductStock(ShoppingCartProductDTO cartProduct, Product product) {
    if (cartProduct.getQuantity() > product.getStockQuantity()) {
      throw new IllegalStateException("Insufficient stock for product: " + product.getName());
    }
  }

  private void adjustProductStock(ShoppingCartProductDTO cartProduct, Product product) {
    product.setStockQuantity(product.getStockQuantity() - cartProduct.getQuantity());
  }

  private void addProductToOrder(Order order, ShoppingCartProductDTO cartProduct, Product product) {
    OrderProduct orderProduct = new OrderProduct(order, product);
    orderProduct.setQuantity(cartProduct.getQuantity());
    orderProduct.setUnitPrice(product.getPrice());
    order.getProducts().add(orderProduct);
  }

  private void linkPaymentAndShipping(Order savedOrder, double totalCost, double shipping) {
    Payment payment = paymentService.createPaymentEntity(savedOrder, totalCost, PaymentStatus.PENDING.name());
    savedOrder.setPayment(payment);

    Shipping shippingEntity = shippingService.createShippingEntity(savedOrder, shipping, LocalDate.now().plusDays(5));
    savedOrder.setShipping(shippingEntity);
  }

  private void validateOrderCancelable(Order order) {
    if (!(OrderStatus.NOT_SHIPPED.name().equals(order.getStatus()) ||
          OrderStatus.WAIT_FOR_PAYMENT.name().equals(order.getStatus()))) {
      throw new IllegalStateException("Only orders with status 'NOT_SHIPPED' or 'WAIT_FOR_PAYMENT' can be cancelled.");
    }
  }

  private void returnProductsToStock(Order order) {
    order.getProducts().forEach(orderProduct -> {
      Product product = orderProduct.getProduct();
      product.setStockQuantity(product.getStockQuantity() + orderProduct.getQuantity());
    });
  }

  private void validateOrderStatusForUpdate(Order order) {
    if (!OrderStatus.NOT_SHIPPED.name().equals(order.getStatus())) {
      throw new IllegalStateException("Only orders with status 'NOT_SHIPPED' can be updated to next status.");
    }
  }
}
