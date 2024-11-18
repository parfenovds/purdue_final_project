package com.parfenov.purdue_final.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity(name = "OrderProduct")
@Table(name = "order_product")
public class OrderProduct {
  @EmbeddedId
  private OrderProductId id;

  @Column(nullable = false)
  private Integer quantity;

  @Column(name = "unit_price", nullable = false)
  private Double unitPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("orderId")
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("productId")
  private Product product;

  public OrderProduct() {};
  public OrderProduct(Order order, Product product) {
    this.order = order;
    this.product = product;
    this.id = new OrderProductId(order.getId(), product.getId());
  }

  public OrderProductId getId() {
    return id;
  }

  public void setId(OrderProductId id) {
    this.id = id;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
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
    if (!(o instanceof OrderProduct that)) {
      return false;
    }
    return Objects.equals(order, that.order) && Objects.equals(product, that.product);
  }

  @Override
  public int hashCode() {
    return Objects.hash(order, product);
  }
}
