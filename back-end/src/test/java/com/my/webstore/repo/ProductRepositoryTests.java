package com.my.webstore.repo;

import com.my.webstore.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

/**
 * The class will test the all CRUD operations of the product entity by following methods,
 *
 * {@link #testCreate()}: test the product insert ({@link ProductRepository#save(Object)})
 * {@link #testRead()}: test the product select ({@link ProductRepository#findAll()})
 * {@link #testUpdate()}: test the product update ({@link ProductRepository#save(Object)})
 * {@link #testDelete()}: test the product delete ({@link ProductRepository#delete(Object)})
 *
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql("/db/master-data.sql")
public class ProductRepositoryTests {
    public static final String PRODUCT_NAME_1 = "Penguin-ears";
    public static final String PRODUCT_NAME_2 = "Horseshoe";

    @Autowired
    private ProductRepository repository;

    @Test
    @Order(1)
    public void testCreate() {
        final Product product = new Product(PRODUCT_NAME_1);
        repository.save(product);
        Assertions.assertThat(product.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void testRead() {
        final List<Product> products = repository.findAll();
        Assertions.assertThat(products).isNotEmpty();
    }

    @Test
    @Order(3)
    public void testUpdate() {
        final Product product = repository.findAll().get(0);
        product.setName(PRODUCT_NAME_2);
        Assertions.assertThat(repository.save(product).getName()).isEqualTo(PRODUCT_NAME_2);
    }

    @Test
    @Order(4)
    public void testDelete() {
        final List<Product> products = repository.findAll();
        final Product product = products.get(products.size() - 1);
        repository.delete(product);
        Assertions.assertThat(repository.findById(product.getId()).isPresent()).isFalse();
    }
}
