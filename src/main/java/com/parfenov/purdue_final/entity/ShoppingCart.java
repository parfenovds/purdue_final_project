package com.parfenov.purdue_final.entity;

import jakarta.persistence.CascadeType;
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

  @OneToMany(mappedBy = "shoppingCart",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
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

//  public void addProduct(Product product, Integer quantity) {
//    ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct(this, product);
//    shoppingCartProduct.setQuantity(quantity);
//    products.add(shoppingCartProduct);
//    product.getShoppingCarts().add(shoppingCartProduct);
//  }

  public void addProduct(Product product, Integer quantity) {
    // Проверяем, существует ли продукт в корзине
    ShoppingCartProduct existingProduct = products.stream()
        .filter(p -> p.getProduct().equals(product))
        .findFirst()
        .orElse(null);

    if (existingProduct != null) {
      // Если продукт уже есть, обновляем количество
      existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
    } else {
      // Если продукта нет, создаем новый объект
      ShoppingCartProduct shoppingCartProduct = new ShoppingCartProduct(this, product);
      shoppingCartProduct.setQuantity(quantity);
      products.add(shoppingCartProduct);
    }
  }


  public void removeProduct(Product product, Integer quantityToRemove) {
    for (Iterator<ShoppingCartProduct> iterator = products.iterator(); iterator.hasNext(); ) {
      ShoppingCartProduct shoppingCartProduct = iterator.next();

      if (shoppingCartProduct.getShoppingCart().equals(this) &&
          shoppingCartProduct.getProduct().equals(product)) {

        int currentQuantity = shoppingCartProduct.getQuantity();
        if (currentQuantity > quantityToRemove) {
          // Уменьшаем количество, если удаляемая часть меньше текущей
          shoppingCartProduct.setQuantity(currentQuantity - quantityToRemove);
        } else {
          // Полностью удаляем продукт, если количество <= удаляемому
          iterator.remove();
          shoppingCartProduct.getProduct().getShoppingCarts().remove(shoppingCartProduct);
          shoppingCartProduct.setProduct(null);
          shoppingCartProduct.setShoppingCart(null);
        }
        break; // Достаточно обработать один раз, так как продукты уникальны
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
