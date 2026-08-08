-- =========================================================
-- HOTEL MANAGEMENT SYSTEM
-- COMPLETE MYSQL DATABASE SETUP
-- =========================================================

-- ---------------------------------------------------------
-- 1. CREATE DATABASE
-- ---------------------------------------------------------

CREATE DATABASE IF NOT EXISTS dg_hotel
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE dg_hotel;


-- ---------------------------------------------------------
-- 2. CREATE APPLICATION USER
-- ---------------------------------------------------------

CREATE USER IF NOT EXISTS 'demo'@'localhost'
IDENTIFIED BY 'Hotel@12345';

ALTER USER 'demo'@'localhost'
IDENTIFIED BY 'Hotel@12345';

GRANT ALL PRIVILEGES ON dg_hotel.* TO 'demo'@'localhost';

FLUSH PRIVILEGES;


-- =========================================================
-- 3. USERS TABLE
-- =========================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
) ENGINE=InnoDB;


-- =========================================================
-- 4. CUSTOMERS TABLE
-- =========================================================

CREATE TABLE IF NOT EXISTS customers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(30),
    address VARCHAR(255),

    PRIMARY KEY (id)
) ENGINE=InnoDB;


-- =========================================================
-- 5. ROOMS TABLE
-- =========================================================

CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT NOT NULL AUTO_INCREMENT,
    room_number VARCHAR(50) NOT NULL,
    room_type VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_rooms_room_number (room_number)
) ENGINE=InnoDB;


-- =========================================================
-- 6. RESERVATIONS TABLE
-- =========================================================

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    status VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_reservation_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_reservation_room
        FOREIGN KEY (room_id)
        REFERENCES rooms(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;


-- =========================================================
-- 7. PAYMENTS TABLE
-- =========================================================

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT fk_payment_reservation
        FOREIGN KEY (reservation_id)
        REFERENCES reservations(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;


-- =========================================================
-- 8. SAMPLE USERS
-- =========================================================
-- Password values below are BCrypt hashes.
-- These are example passwords for development only.
--
-- admin / password
-- staff / password
--
-- Change them before production use.

INSERT INTO users (username, password, role)
VALUES
(
    'admin',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoO8kX7zY1YqY7mM0LQ6qJ6j7Y9Y6xQW5K',
    'ADMIN'
),
(
    'staff',
    '$2a$10$7EqJtq98hPqEX7fNZaFWoO8kX7zY1YqY7mM0LQ6qJ6j7Y9Y6xQW5K',
    'STAFF'
)
ON DUPLICATE KEY UPDATE
    username = VALUES(username);


-- =========================================================
-- 9. SAMPLE CUSTOMERS
-- =========================================================

INSERT INTO customers
    (name, email, phone, address)
VALUES
    ('John Kamau', 'john@example.com', '0712345678', 'Nairobi'),
    ('Mary Wanjiku', 'mary@example.com', '0723456789', 'Kiambu'),
    ('Peter Otieno', 'peter@example.com', '0734567890', 'Kisumu');


-- =========================================================
-- 10. SAMPLE ROOMS
-- =========================================================

INSERT INTO rooms
    (room_number, room_type, price, status)
VALUES
    ('101', 'SINGLE', 3000.00, 'AVAILABLE'),
    ('102', 'SINGLE', 3000.00, 'AVAILABLE'),
    ('201', 'DOUBLE', 5000.00, 'AVAILABLE'),
    ('202', 'DOUBLE', 5000.00, 'AVAILABLE'),
    ('301', 'DELUXE', 7500.00, 'AVAILABLE'),
    ('302', 'DELUXE', 7500.00, 'AVAILABLE'),
    ('401', 'SUITE', 12000.00, 'AVAILABLE');


-- =========================================================
-- 11. SAMPLE RESERVATION
-- =========================================================

INSERT INTO reservations
    (customer_id, room_id, check_in, check_out, status)
VALUES
    (1, 1, '2026-08-10', '2026-08-12', 'CONFIRMED'),
    (2, 3, '2026-08-15', '2026-08-18', 'CONFIRMED');


-- =========================================================
-- 12. SAMPLE PAYMENTS
-- =========================================================

INSERT INTO payments
    (reservation_id, amount, payment_method, status)
VALUES
    (1, 6000.00, 'CASH', 'PAID'),
    (2, 15000.00, 'MPESA', 'PAID');


-- =========================================================
-- 13. VERIFY DATABASE
-- =========================================================

SELECT DATABASE();

SHOW TABLES;


-- =========================================================
-- 14. VIEW CREATED DATA
-- =========================================================

SELECT * FROM users;

SELECT * FROM customers;

SELECT * FROM rooms;

SELECT * FROM reservations;

SELECT * FROM payments;