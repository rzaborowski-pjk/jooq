CREATE TABLE IF NOT EXISTS product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    price       NUMERIC(19, 2) NOT NULL,
    quantity    INTEGER,
    sku         VARCHAR(255),
    date        TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS roles
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS product_roles
(
    product_id BIGINT NOT NULL,
    role_id    BIGINT NOT NULL,
    PRIMARY KEY (product_id, role_id),
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

INSERT INTO product (name, description, price, quantity, sku, date)
VALUES ('Laptop Pro 15', 'Wydajny laptop do pracy i rozrywki', 5999.99, 12, 'LP15-PRO-001', NOW()),
       ('Smartphone X', 'Nowoczesny smartfon z aparatem 108MP', 3999.50, 25, 'SM-X-002', NOW()),
       ('Bezprzewodowe słuchawki', 'Bluetooth 5.2, aktywna redukcja szumów', 699.99, 50, 'HP-BT-003', NOW()),
       ('Monitor 27"', 'Monitor QHD 27 cali z matrycą IPS', 1199.00, 8, 'MN-27QHD-004', NOW()),
       ('Klawiatura mechaniczna', 'RGB, przełączniki RED', 349.99, 30, 'KB-MECH-005', NOW()),
       ('Mysz gamingowa', 'Sensor 16 000 DPI, 8 przycisków', 249.99, 45, 'GM-MOUSE-006', NOW()),
       ('Stacja dokująca', 'USB-C, HDMI, Ethernet', 499.99, 10, 'DK-STATION-007', NOW()),
       ('Powerbank 20k mAh', 'USB-C PD, szybkie ładowanie', 199.90, 60, 'PB-20K-008', NOW()),
       ('Tablet 10"', 'FullHD, 64GB pamięci', 1099.99, 14, 'TB-10FHD-009', NOW()),
       ('Głośnik Bluetooth', 'Wodoodporny, 20W mocy', 299.00, 35, 'SPK-BT-010', NOW());


INSERT INTO roles (name, description)
VALUES ('Admin', 'Administrator role'),
       ('Editor', 'Editor role'),
       ('Viewer', 'Viewer role');

INSERT INTO product_roles (product_id, role_id)
VALUES (1, 1), -- Produkt o ID 1 ma rolę Admin
       (1, 2), -- Produkt o ID 1 ma rolę Editor
       (2, 3); -- Produkt o ID 2 ma rolę Viewer