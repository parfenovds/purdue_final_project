-- PostgreSQL Script

-- Table pgu_shop.customer
CREATE TABLE IF NOT EXISTS customer
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(45)  NOT NULL,
    email    VARCHAR(128) NOT NULL,
    password VARCHAR(256) NOT NULL,
    role     VARCHAR(45)  NOT NULL
);

-- Table pgu_shop.orders
CREATE TABLE IF NOT EXISTS orders
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    status      VARCHAR(20),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);

-- Table pgu_shop.product
CREATE TABLE IF NOT EXISTS product
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255)     NOT NULL,
    description    TEXT,
    price          DOUBLE PRECISION NOT NULL,
    stock_quantity INT              NOT NULL
);

-- Table pgu_shop.shipping
CREATE TABLE IF NOT EXISTS shipping
(
    id                      BIGSERIAL PRIMARY KEY,
    order_id                BIGINT           NOT NULL,
    shipping_cost           DOUBLE PRECISION NOT NULL,
    estimated_delivery_date DATE,
    tracking_number         VARCHAR(100),
    CONSTRAINT fk_shipping_orders FOREIGN KEY (order_id) REFERENCES orders (id)
);

-- Table pgu_shop.payment
CREATE TABLE IF NOT EXISTS payment
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT           NOT NULL,
    amount         DOUBLE PRECISION NOT NULL,
    payment_status VARCHAR(20),
    payment_date   TIMESTAMP,
    CONSTRAINT fk_payment_orders FOREIGN KEY (order_id) REFERENCES orders (id)
);

-- Table pgu_shop.shopping_cart
CREATE TABLE IF NOT EXISTS shopping_cart
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    CONSTRAINT fk_shopping_cart_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);

-- Table pgu_shop.order_product
CREATE TABLE IF NOT EXISTS order_product
(
    order_id   BIGINT           NOT NULL,
    product_id BIGINT           NOT NULL,
    quantity   INT              NOT NULL,
    unit_price DOUBLE PRECISION NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_product_orders FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_product_product FOREIGN KEY (product_id) REFERENCES product (id)
);

-- Table pgu_shop.shopping_cart_product
CREATE TABLE IF NOT EXISTS shopping_cart_product
(
    shopping_cart_id BIGINT NOT NULL,
    product_id       BIGINT NOT NULL,
    quantity         INT    NOT NULL,
    PRIMARY KEY (shopping_cart_id, product_id),
    CONSTRAINT fk_shopping_cart_product_shopping_cart FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart (id),
    CONSTRAINT fk_shopping_cart_product_product FOREIGN KEY (product_id) REFERENCES product (id)
);
