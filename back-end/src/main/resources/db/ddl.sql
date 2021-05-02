CREATE DATABASE web_store;
\c web_store;
CREATE TABLE product
(
    id INTEGER GENERATED ALWAYS AS IDENTITY,
    name CHARACTER VARYING(15) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE product_carton
(
    id INTEGER GENERATED ALWAYS AS IDENTITY,
    product_id INTEGER NOT NULL,
    no_of_units INTEGER NOT NULL,
    price NUMERIC(11, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_product
       FOREIGN KEY(product_id)
	   REFERENCES product(id)
);