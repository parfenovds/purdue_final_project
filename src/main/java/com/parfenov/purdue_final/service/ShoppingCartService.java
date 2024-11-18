package com.parfenov.purdue_final.service;

import com.parfenov.purdue_final.dto.ShoppingCartDTO;
import com.parfenov.purdue_final.entity.Customer;
import com.parfenov.purdue_final.entity.Product;
import com.parfenov.purdue_final.entity.ShoppingCart;
import com.parfenov.purdue_final.entity.ShoppingCartProduct;
import com.parfenov.purdue_final.entity.ShoppingCartProductId;
import com.parfenov.purdue_final.exception.NotFoundException;
import com.parfenov.purdue_final.mapper.ShoppingCartMapper;
import com.parfenov.purdue_final.repository.CustomerRepository;
import com.parfenov.purdue_final.repository.ProductRepository;
import com.parfenov.purdue_final.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
  private final ShoppingCartRepository shoppingCartRepository;
  private final ProductRepository productRepository;
  private final ShoppingCartMapper shoppingCartMapper;
  private final CustomerRepository customerRepository;

  @Transactional(readOnly = true)
  public ShoppingCartDTO getProductsInCart() {
    String currentUsername = getCurrentUsername();
    Customer customer = getCustomer(currentUsername);

    ShoppingCart shoppingCart = getShoppingCart(customer);
    return shoppingCartMapper.toDto(shoppingCart);
  }

  @Transactional
  public ShoppingCartDTO addProductToCart(Long productId, Integer quantity) {
    String currentUsername = getCurrentUsername();
    Customer customer = getCustomer(currentUsername);

    ShoppingCart shoppingCart = getShoppingCart(customer);
    Product product = getProduct(productId);

    // Логика добавления продукта
    ShoppingCartProduct existingProduct = shoppingCart.getProducts().stream()
        .filter(p -> p.getProduct().getId().equals(productId))
        .findFirst()
        .orElse(null);

    if (existingProduct != null) {
      // Если продукт уже есть, увеличиваем количество
      int newQuantity = existingProduct.getQuantity() + quantity;
      checkQuantity(newQuantity, product);
      existingProduct.setQuantity(newQuantity);
    } else {
      // Добавляем новый продукт
      shoppingCart.addProduct(product, quantity);
    }

    ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);
    return shoppingCartMapper.toDto(savedCart);
  }

  @Transactional
  public ShoppingCartDTO removeProductFromCart(Long productId, Integer quantityToRemove) {
    String currentUsername = getCurrentUsername();
    Customer customer = getCustomer(currentUsername);

    ShoppingCart shoppingCart = getShoppingCart(customer);
    Product product = getProduct(productId);

    // Удаляем продукт с учетом количества
    shoppingCart.removeProduct(product, quantityToRemove);

    ShoppingCart savedCart = shoppingCartRepository.save(shoppingCart);

    return shoppingCartMapper.toDto(savedCart);
  }

  // ===================== Вспомогательные методы =====================

  private String getCurrentUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  private Customer getCustomer(String currentUsername) {
    return customerRepository.findByEmail(currentUsername)
        .orElseThrow(() -> new NotFoundException("Customer not found with email: " + currentUsername));
  }

  private ShoppingCart getShoppingCart(Customer customer) {
    return shoppingCartRepository.findByCustomerId(customer.getId())
        .orElseGet(() -> shoppingCartRepository.save(new ShoppingCart(customer)));
  }

  private Product getProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));
  }

  private ShoppingCartProduct getShoppingCartProduct(Long productId, ShoppingCart shoppingCart) {
    ShoppingCartProductId compositeId = new ShoppingCartProductId(shoppingCart.getId(), productId);
    return shoppingCart.getProducts().stream()
        .filter(cartProduct -> cartProduct.getId().equals(compositeId))
        .findFirst()
        .orElse(null);
  }


  private void checkQuantity(Integer quantity, Product product) {
    if (quantity > product.getStockQuantity()) {
      throw new IllegalStateException("Not enough stock for product: " + product.getName());
    }
  }
}
