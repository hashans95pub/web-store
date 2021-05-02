package com.my.webstore;

import com.my.webstore.controller.v1.WebStoreController;
import com.my.webstore.repo.ProductCartonRepository;
import com.my.webstore.repo.ProductRepository;
import com.my.webstore.service.PriceService;
import com.my.webstore.service.ProductCartonService;
import com.my.webstore.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WebStoreApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCartonRepository productCartonRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCartonService productCartonService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private WebStoreController webStoreController;


    /**
     * The test will validate all required beans are created.
     */
    @Test
    void contextLoads() {
        Assertions.assertThat(productRepository).isNotNull();
        Assertions.assertThat(productCartonRepository).isNotNull();
        Assertions.assertThat(productService).isNotNull();
        Assertions.assertThat(productCartonService).isNotNull();
        Assertions.assertThat(priceService).isNotNull();
        Assertions.assertThat(webStoreController).isNotNull();
    }

}
