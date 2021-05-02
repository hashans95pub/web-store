package com.my.webstore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The model class represents product related properties
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 15, nullable = false)
    private String name;

    public Product(int id) {
        this.id = id;
    }

    public Product(String name) {
        this.name = name;
    }
}
