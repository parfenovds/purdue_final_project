package com.parfenov.purdue_final.controller;

import com.parfenov.purdue_final.dto.ProductDTO;
import com.parfenov.purdue_final.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "Get all products", description = "Retrieve a list of all available products.")
  @GetMapping
  public ResponseEntity<List<ProductDTO>> getAllProducts() {
    List<ProductDTO> products = productService.findAll();
    return ResponseEntity.ok(products);
  }

  @Operation(summary = "Get product by ID", description = "Retrieve details of a specific product by its ID.")
  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> getProductById(@PathVariable @Parameter(description = "ID of the product") Long id) {
    ProductDTO product = productService.findById(id);
    return ResponseEntity.ok(product);
  }

  @Operation(summary = "Create a product", description = "Create a new product (ADMIN only).")
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductDTO> createProduct(@RequestBody @Parameter(description = "Product data") ProductDTO productDTO) {
    ProductDTO createdProduct = productService.save(productDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
  }

  @Operation(summary = "Update a product", description = "Update details of an existing product (ADMIN only).")
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ProductDTO> updateProduct(@PathVariable @Parameter(description = "ID of the product") Long id,
      @RequestBody @Parameter(description = "Updated product data") ProductDTO productDTO) {
    ProductDTO updatedProduct = productService.update(id, productDTO);
    return ResponseEntity.ok(updatedProduct);
  }

  @Operation(summary = "Delete a product", description = "Delete a product by its ID (ADMIN only).")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteProduct(@PathVariable @Parameter(description = "ID of the product") Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
