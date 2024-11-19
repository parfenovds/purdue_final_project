CREATE TABLE IF NOT EXISTS customer
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(45)  NOT NULL UNIQUE,
    email    VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    role     VARCHAR(45)  NOT NULL
);

CREATE TABLE IF NOT EXISTS orders
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    status      VARCHAR(20),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE IF NOT EXISTS product
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255)     NOT NULL,
    description    TEXT,
    price          DOUBLE PRECISION NOT NULL,
    stock_quantity INT              NOT NULL
);

CREATE TABLE IF NOT EXISTS shipping
(
    id                      BIGSERIAL PRIMARY KEY,
    order_id                BIGINT           NOT NULL,
    shipping_cost           DOUBLE PRECISION NOT NULL,
    estimated_delivery_date DATE,
    tracking_number         VARCHAR(100),
    CONSTRAINT fk_shipping_orders FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE IF NOT EXISTS payment
(
    id             BIGSERIAL PRIMARY KEY,
    order_id       BIGINT           NOT NULL,
    amount         DOUBLE PRECISION NOT NULL,
    payment_status VARCHAR(20),
    payment_date   TIMESTAMP,
    CONSTRAINT fk_payment_orders FOREIGN KEY (order_id) REFERENCES orders (id)
);

CREATE TABLE IF NOT EXISTS shopping_cart
(
    id          BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    CONSTRAINT fk_shopping_cart_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);

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

CREATE TABLE IF NOT EXISTS shopping_cart_product
(
    shopping_cart_id BIGINT NOT NULL,
    product_id       BIGINT NOT NULL,
    quantity         INT    NOT NULL,
    PRIMARY KEY (shopping_cart_id, product_id),
    CONSTRAINT fk_shopping_cart_product_shopping_cart FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart (id),
    CONSTRAINT fk_shopping_cart_product_product FOREIGN KEY (product_id) REFERENCES product (id)
);

--For each user password is 1234
INSERT INTO customer (login, email, password, role)
VALUES
    ('me', 'me@google.com', '$2a$10$nEgmufo0/Hf3EQkLtdJIQ.UzoKF/Vg.VpwS9ODZwgR2Sxw2doupv2', 'ROLE_CUSTOMER'),
    ('admin', 'admin@google.com', '$2a$10$66EINtBm/uelmSDi/61Jp.X8os.JhImyK3oNKRb9tvLUWg.Pk9vSe', 'ROLE_ADMIN'),
    ('second', 'second@google.com', '$2a$10$PNbnyBGkTQYI6TTFd9.72uWaLBGvJbnseTIoDQIs4JiputEm6t2iu', 'ROLE_CUSTOMER');

INSERT INTO product (name, description, price, stock_quantity)
VALUES
    ('Laptop', 'A high-performance laptop for all your computing needs.', 1200.50, 10),
    ('Smartphone', 'Latest model smartphone with cutting-edge features.', 899.99, 25),
    ('Wireless Headphones', 'Noise-cancelling over-ear headphones.', 199.99, 15),
    ('Gaming Console', 'Next-gen gaming console with immersive gameplay.', 499.99, 8),
    ('Smartwatch', 'Feature-rich smartwatch with health tracking.', 149.99, 30),
    ('Tablet', 'Portable tablet with powerful specs.', 349.99, 20);
