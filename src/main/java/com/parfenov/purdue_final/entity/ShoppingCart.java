package com.parfenov.purdue_final.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart implements AbstractEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @OneToMany(mappedBy = "shoppingCart")
  private List<ShoppingCartProduct> products;

  public ShoppingCart() {
  }

  public ShoppingCart(Customer customer) {
    this.customer = customer;
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

  public List<ShoppingCartProduct> getProducts() {
    return products;
  }

  public void setProducts(List<ShoppingCartProduct> products) {
    this.products = products;
  }

  public void addProduct(Product product) {
    ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct();
    products.add(shoppingCartProduct);
    product.getShoppingCarts().add(shoppingCartProduct);
  }

  public void removeProduct(Product product) {
    for (Iterator<ShoppingCartProduct> iterator = products.iterator();
         iterator.hasNext(); ) {
      ShoppingCartProduct shoppingCartProduct = iterator.next();

      if (shoppingCartProduct.getShoppingCart().equals(this) &&
          shoppingCartProduct.getProduct().equals(product)) {
        iterator.remove();
        shoppingCartProduct.getProduct().getOrders().remove(shoppingCartProduct);
        shoppingCartProduct.setProduct(null);
        shoppingCartProduct.setShoppingCart(null);
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ShoppingCart that)) {
      return false;
    }
    return Objects.equals(customer, that.customer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer);
  }
}
