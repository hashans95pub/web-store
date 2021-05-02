package com.my.webstore.service;

import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.Product;
import com.my.webstore.repo.ProductRepository;
import com.my.webstore.service.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * The class will test all product related services provided by {@link ProductServiceImpl} with following methods,
 * <p>
 * {@link #testFindAll()}: test the service of get all products ({@link ProductServiceImpl#findAll()} ()})
 * {@link #testIsExist()}: test the service of check the product exists ({@link ProductServiceImpl#isExist(int)})
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductServiceImpl service;

    @Mock
    ProductRepository repository;

    @Test
    public void testFindAll() throws ServiceException {
        final List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Penguin-ears"));
        productList.add(new Product(2, "Horseshoe"));

        Mockito.when(repository.findAll()).thenReturn(productList);

        final List<Product> products = service.findAll();
        Assertions.assertThat(products.size()).isEqualTo(2); // Check for list size as "2"

        Mockito.verify(repository).findAll(); // Verify the service calls the repository method
    }

    @Test
    public void testIsExist() throws ServiceException {

    }
}
