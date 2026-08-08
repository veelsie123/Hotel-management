-- Initial schema for dg_hotel

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS customer (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  email VARCHAR(150),
  phone VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS room (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  number VARCHAR(20),
  type VARCHAR(50),
  price_per_night DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS reservation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id BIGINT,
  room_id BIGINT,
  check_in DATE,
  check_out DATE,
  CONSTRAINT fk_res_customer FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE SET NULL,
  CONSTRAINT fk_res_room FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS payment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  amount DECIMAL(10,2),
  method VARCHAR(50),
  paid_at DATETIME,
  reservation_id BIGINT UNIQUE,
  CONSTRAINT fk_pay_res FOREIGN KEY (reservation_id) REFERENCES reservation(id) ON DELETE CASCADE
);
