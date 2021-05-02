package com.my.webstore.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The model class represents product carton related properties
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "product_carton")
public class ProductCarton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "no_of_units")
    private int noOfUnits;

    @Column(name = "price", precision = 11, scale = 2, nullable = false)
    private BigDecimal price;

    public ProductCarton(Product product, int noOfUnits, BigDecimal price) {
        this.product = product;
        this.noOfUnits = noOfUnits;
        this.price = price;
    }

    public ProductCarton(int id, int noOfUnits, BigDecimal price) {
        this.id = id;
        this.noOfUnits = noOfUnits;
        this.price = price;
    }
}
