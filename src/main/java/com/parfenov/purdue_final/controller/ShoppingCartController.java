package com.parfenov.purdue_final.controller;

import com.parfenov.purdue_final.dto.ShoppingCartDTO;
import com.parfenov.purdue_final.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Endpoints for managing the shopping cart")
public class ShoppingCartController {

  private final ShoppingCartService shoppingCartService;

  @Operation(summary = "Get products in the cart", description = "Retrieve all products currently in the shopping cart.")
  @GetMapping
  public ResponseEntity<ShoppingCartDTO> getProductsInCart() {
    ShoppingCartDTO shoppingCartDTO = shoppingCartService.getProductsInCart();
    return ResponseEntity.ok(shoppingCartDTO);
  }

  @Operation(summary = "Add product to cart", description = "Add a product to the shopping cart.")
  @PostMapping("/add")
  public ResponseEntity<ShoppingCartDTO> addProductToCart(
      @RequestParam @Parameter(description = "ID of the product") Long productId,
      @RequestParam @Parameter(description = "Quantity to add") Integer quantity
  ) {
    ShoppingCartDTO shoppingCartDTO = shoppingCartService.addProductToCart(productId, quantity);
    return ResponseEntity.ok(shoppingCartDTO);
  }

  @Operation(summary = "Remove product from cart", description = "Remove a product or decrease its quantity in the shopping cart.")
  @DeleteMapping("/remove")
  public ResponseEntity<ShoppingCartDTO> removeProductFromCart(
      @RequestParam @Parameter(description = "ID of the product") Long productId,
      @RequestParam @Parameter(description = "Quantity to remove") Integer quantity) {
    ShoppingCartDTO shoppingCartDTO = shoppingCartService.removeProductFromCart(productId, quantity);
    return ResponseEntity.ok(shoppingCartDTO);
  }
}
