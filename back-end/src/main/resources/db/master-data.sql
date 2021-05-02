INSERT INTO product (name)
VALUES ('Penguin-ears'),
       ('Horseshoe');

INSERT INTO product_carton (product_id, no_of_units, price)
VALUES ((SELECT id FROM product WHERE name = 'Penguin-ears'), 20, 175.00),
       ((SELECT id FROM product WHERE name = 'Horseshoe'), 5, 825.00);