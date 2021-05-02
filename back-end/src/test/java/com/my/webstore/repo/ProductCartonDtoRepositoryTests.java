package com.my.webstore.repo;

import com.my.webstore.model.Product;
import com.my.webstore.model.ProductCarton;
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

import java.math.BigDecimal;

/**
 *
 * The class will test the all CRUD operations of the product carton entity by following methods,
 *
 * {@link #testCreate()}: test the product carton insert ({@link ProductCartonRepository#save(Object)})
 * {@link #testRead()}: test the product carton select ({@link ProductCartonRepository#findAll()})
 * {@link #testUpdate()}: test the product carton update ({@link ProductCartonRepository#save(Object)})
 * {@link #testDelete()}: test the product carton delete ({@link ProductCartonRepository#delete(Object)})
 *
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql("/db/master-data.sql")
public class ProductCartonDtoRepositoryTests {

    @Autowired
    private ProductCartonRepository repository;

    @Test
    @Order(1)
    public void testCreate() {
        final ProductCarton carton = new ProductCarton(
                new Product(1),
                8,
                new BigDecimal("100.00")
        );
        repository.save(carton);
        Assertions.assertThat(carton.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void testRead() {
        Assertions.assertThat(repository.findAll()).isNotEmpty();
    }

    @Test
    @Order(3)
    public void testUpdate() {
        final ProductCarton carton = repository.findAll().get(0);
        carton.setNoOfUnits(10);
        Assertions.assertThat(repository.save(carton).getNoOfUnits()).isEqualTo(10);
    }

    @Test
    @Order(4)
    public void testDelete() {
        repository.deleteAll();
        Assertions.assertThat(repository.findAll()).isEmpty();
    }
}
