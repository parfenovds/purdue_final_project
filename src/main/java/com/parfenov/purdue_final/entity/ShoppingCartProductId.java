package com.parfenov.purdue_final.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ShoppingCartProductId implements Serializable {
  @Column(name = "shopping_cart_id")
  private Long shoppingCartId;
  @Column(name = "product_id")
  private Long productId;

  public ShoppingCartProductId() {
  }

  public ShoppingCartProductId(Long shoppingCartId, Long productId) {
    this.shoppingCartId = shoppingCartId;
    this.productId = productId;
  }

  public Long getShoppingCartId() {
    return shoppingCartId;
  }

  public void setShoppingCartId(Long shoppingCartId) {
    this.shoppingCartId = shoppingCartId;
  }

  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ShoppingCartProductId that)) {
      return false;
    }
    return Objects.equals(shoppingCartId, that.shoppingCartId) && Objects.equals(productId, that.productId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shoppingCartId, productId);
  }
}
