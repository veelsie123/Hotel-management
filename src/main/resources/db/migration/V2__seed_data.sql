-- Seed initial rooms and customers

INSERT INTO customer (first_name, last_name, email, phone) VALUES ('John', 'Doe', 'john@example.com', '555-0100');
INSERT INTO customer (first_name, last_name, email, phone) VALUES ('Jane', 'Smith', 'jane@example.com', '555-0101');

INSERT INTO room (number, type, price_per_night) VALUES ('101', 'SINGLE', 75.00);
INSERT INTO room (number, type, price_per_night) VALUES ('102', 'DOUBLE', 120.00);
