package com.parfenov.purdue_final.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order implements AbstractEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @Column(length = 20)
  private String status;

  @OneToMany(
      mappedBy = "order",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private List<OrderProduct> products = new ArrayList<>();

  @OneToOne(mappedBy = "order")
  private Shipping shipping;

  @OneToOne(mappedBy = "order")
  private Payment payment;

  public Order() {
  }

  public Order(Customer customer, String status, Shipping shipping, Payment payment) {
    this.customer = customer;
    this.status = status;
    this.shipping = shipping;
    this.payment = payment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<OrderProduct> getProducts() {
    return products;
  }

  public void setProducts(List<OrderProduct> products) {
    this.products = products;
  }

  public Shipping getShipping() {
    return shipping;
  }

  public void setShipping(Shipping shipping) {
    this.shipping = shipping;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public void addProduct(Product product) {
    OrderProduct orderProduct = new OrderProduct(this, product);
    products.add(orderProduct);
    product.getOrders().add(orderProduct);
  }

  public void removeProduct(Product product) {
    for (Iterator<OrderProduct> iterator = products.iterator();
         iterator.hasNext(); ) {
      OrderProduct orderProduct = iterator.next();

      if (orderProduct.getOrder().equals(this) &&
          orderProduct.getProduct().equals(product)) {
        iterator.remove();
        orderProduct.getProduct().getOrders().remove(orderProduct);
        orderProduct.setProduct(null);
        orderProduct.setOrder(null);
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Order order)) {
      return false;
    }
    return Objects.equals(id, order.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
