package com.parfenov.purdue_final.controller;

import com.parfenov.purdue_final.dto.ShoppingCartDTO;
import com.parfenov.purdue_final.entity.ShoppingCart;
import com.parfenov.purdue_final.entity.ShoppingCartProduct;
import com.parfenov.purdue_final.service.ShoppingCartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

  private final ShoppingCartService shoppingCartService;

  @GetMapping
  public ResponseEntity<ShoppingCartDTO> getProductsInCart() {
    ShoppingCartDTO shoppingCartDTO = shoppingCartService.getProductsInCart();
    return ResponseEntity.ok(shoppingCartDTO);
  }

  @PostMapping("/add")
  public ResponseEntity<ShoppingCartDTO> addProductToCart(
      @RequestParam Long productId,
      @RequestParam Integer quantity
  ) {
    ShoppingCartDTO shoppingCartDTO = shoppingCartService.addProductToCart(productId, quantity);
    return ResponseEntity.ok(shoppingCartDTO);
  }

  @DeleteMapping("/remove")
  public ResponseEntity<ShoppingCartDTO> removeProductFromCart(
      @RequestParam Long productId,
      @RequestParam Integer quantity) {
    ShoppingCartDTO shoppingCartDTO = shoppingCartService.removeProductFromCart(productId, quantity);
    return ResponseEntity.ok(shoppingCartDTO);
  }
}
