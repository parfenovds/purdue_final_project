package com.parfenov.purdue_final.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "shopping_cart_product")
public class ShoppingCartProduct {
  @EmbeddedId
  private ShoppingCartProductId id;

  @Column(nullable = false)
  private Integer quantity;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("shoppingCartId")
  private ShoppingCart shoppingCart;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  private Product product;

  public ShoppingCartProduct() {
  }

  public ShoppingCartProduct(ShoppingCart shoppingCart, Product product) {
    this.shoppingCart = shoppingCart;
    this.product = product;
    this.id = new ShoppingCartProductId(shoppingCart.getId(), product.getId());
  }

  public ShoppingCartProductId getId() {
    return id;
  }

  public void setId(ShoppingCartProductId id) {
    this.id = id;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public ShoppingCart getShoppingCart() {
    return shoppingCart;
  }

  public void setShoppingCart(ShoppingCart shoppingCart) {
    this.shoppingCart = shoppingCart;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ShoppingCartProduct that)) {
      return false;
    }
    return Objects.equals(shoppingCart, that.shoppingCart) && Objects.equals(product, that.product);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shoppingCart, product);
  }
}
