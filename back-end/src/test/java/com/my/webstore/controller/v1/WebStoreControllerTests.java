package com.my.webstore.controller.v1;

import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.Product;
import com.my.webstore.model.ProductCarton;
import com.my.webstore.service.PriceService;
import com.my.webstore.service.ProductCartonService;
import com.my.webstore.service.ProductService;
import com.my.webstore.util.MessageSourceUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The class will test the all ReST services exposed by {@link WebStoreController} with following methods,
 * <p>
 * {@link #testGetProducts()}: test the service of get all products ({@link WebStoreController#getProducts()})
 * {@link #testGetPriceList()}: test the service of get price-list ({@link WebStoreController#getPriceList(int, short)})
 * {@link #testGetProductCartons()}: test the service of get cartons of a particular product ({@link WebStoreController#getProductCartons(int)})
 * {@link #testGetUnitsPrice()}: test the service of get price for units of a product ({@link WebStoreController#getUnitsPrice(int, short)})
 * {@link #testGetCartonsPrice()}: test the service of get price for cartons of a product ({@link WebStoreController#getCartonsPrice(int, short)})
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(WebStoreController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WebStoreControllerTests {
    public static final String BASE_URI = "/api/v1";

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductCartonService cartonService;

    @MockBean
    private PriceService priceService;

    @MockBean
    private MessageSourceUtil messageSourceUtil;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testGetProducts() throws Exception {
        // Test the success scenario
        final List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Penguin-ears"));
        productList.add(new Product(2, "Horseshoe"));

        Mockito.when(productService.findAll()).thenReturn(productList);

        mockMvc.perform(get(BASE_URI.concat("/products")))
                .andExpect(status().isOk()) // Check for request success(200)
                .andExpect(jsonPath("$.status", Matchers.is(1))) // Check for success status
                .andExpect(jsonPath("$.result", Matchers.hasSize(2))) // Check for product count as 2
                .andExpect(jsonPath("$.result[0].name",
                        Matchers.is("Penguin-ears"))); // Check for first product name as "Penguin-ears"

        // Test the fail scenario
        Mockito.when(productService.findAll()).thenThrow(new ServiceException(ServiceException.PROCESSING_FAILED,
                "Error while fetching data"));

        mockMvc.perform(get(BASE_URI.concat("/products")))
                .andExpect(status().isInternalServerError()) // Check for internal server error(500)
                .andExpect(jsonPath("$.status", Matchers.is(0))) // Check for fail status
                .andExpect(jsonPath("$.errorMsg",
                        Matchers.is("Error while fetching data"))); // Check for error message
    }

    @Test
    @Order(2)
    public void testGetPriceList() throws Exception {
        // Test the success scenario
        Mockito.when(productService.isExist(1)).thenReturn(true);
        Mockito.when(priceService.calculateForUnits(1, 1)).thenReturn(BigDecimal.valueOf(11.37));
        Mockito.when(priceService.calculateForUnits(1, 2)).thenReturn(BigDecimal.valueOf(22.74));
        Mockito.when(priceService.calculateForUnits(1, 3)).thenReturn(BigDecimal.valueOf(34.11));
        Mockito.when(priceService.calculateForUnits(1, 4)).thenReturn(BigDecimal.valueOf(45.48));
        Mockito.when(priceService.calculateForUnits(1, 5)).thenReturn(BigDecimal.valueOf(56.85));

        mockMvc.perform(get(BASE_URI.concat("/products/1/price-list?for=5")))
                .andExpect(status().isOk()) // Check for request success(200)
                .andExpect(jsonPath("$.status", Matchers.is(1))) // Check for success status
                .andExpect(jsonPath("$.result", Matchers.hasSize(5))) // Check for product count as 5
                .andExpect(jsonPath("$.result[0].price",
                        Matchers.is(11.37))); // Check for first price as "11.37"

        // Test the fail scenarios
        // Test processing fail
        Mockito.when(priceService.calculateForUnits(1, 2))
                .thenThrow(new ServiceException(ServiceException.PROCESSING_FAILED, "Error while fetching data"));

        mockMvc.perform(get(BASE_URI.concat("/products/1/price-list?for=5")))
                .andExpect(status().isInternalServerError()) // Check for internal server error(500)
                .andExpect(jsonPath("$.status", Matchers.is(0))) // Check for fail status
                .andExpect(jsonPath("$.errorMsg",
                        Matchers.is("Error while fetching data"))); // Check for error message

        // Test validation fail
        Mockito.when(priceService.calculateForUnits(1, 1))
                .thenThrow(new ServiceException(ServiceException.VALIDATION_FAILED,
                        "label.web.store.val.error.no.carton.available"));
        Mockito.when(messageSourceUtil.getMessage("label.web.store.val.error.no.carton.available", new Object[0]))
                .thenReturn("No carton(s) available for the product");

        mockMvc.perform(get(BASE_URI.concat("/products/1/price-list?for=5")))
                .andExpect(status().isOk()) // Check for request success(200)
                .andExpect(jsonPath("$.status", Matchers.is(0))) // Check for fail status
                .andExpect(jsonPath("$.errorMsg",
                        Matchers.is("No carton(s) available for the product"))); // Check for error message

        // Test product not found
        Mockito.when(productService.isExist(3)).thenReturn(false);

        mockMvc.perform(get(BASE_URI.concat("/products/3/price-list?for=5")))
                .andExpect(status().isNotFound()); // Check for result not found(404)
    }

    @Test
    @Order(3)
    public void testGetProductCartons() throws Exception {
        // Test the success scenario
        final List<ProductCarton> cartonList = new ArrayList<>();
        cartonList.add(new ProductCarton(1, 20, BigDecimal.valueOf(175)));

        Mockito.when(productService.isExist(1)).thenReturn(true);
        Mockito.when(cartonService.findAllByProductId(1)).thenReturn(cartonList);

        mockMvc.perform(get(BASE_URI.concat("/products/1/cartons")))
                .andExpect(status().isOk()) // Check for request success(200)
                .andExpect(jsonPath("$.status", Matchers.is(1))) // Check for success status
                .andExpect(jsonPath("$.result", Matchers.hasSize(1))) // Check for product count as 1
                .andExpect(jsonPath("$.result[0].noOfUnits",
                        Matchers.is(20))); // Check for first carton's no of units as "20"

        // Test the fail scenarios
        // Test processing fail
        Mockito.when(cartonService.findAllByProductId(1)).thenThrow(new ServiceException(ServiceException.PROCESSING_FAILED,
                "Error while fetching data"));

        mockMvc.perform(get(BASE_URI.concat("/products/1/cartons")))
                .andExpect(status().isInternalServerError()) // Check for internal server error(500)
                .andExpect(jsonPath("$.status", Matchers.is(0))) // Check for fail status
                .andExpect(jsonPath("$.errorMsg",
                        Matchers.is("Error while fetching data"))); // Check for error message

        // Test product not found
        Mockito.when(productService.isExist(3)).thenReturn(false);

        mockMvc.perform(get(BASE_URI.concat("/products/3/cartons")))
                .andExpect(status().isNotFound()); // Check for result not found(404)
    }

    @Test
    @Order(4)
    public void testGetUnitsPrice() throws Exception {
        // Test the success scenario
        Mockito.when(productService.isExist(1)).thenReturn(true);
        Mockito.when(priceService.calculateForUnits(1, 25)).thenReturn(BigDecimal.valueOf(231.85));

        mockMvc.perform(get(BASE_URI.concat("/products/1/prices?noOfUnits=25")))
                .andExpect(status().isOk()) // Check for request success(200)
                .andExpect(jsonPath("$.status", Matchers.is(1))) // Check for success status
                .andExpect(jsonPath("$.result", Matchers.is(231.85))); // Check for price as "231.85"

        // Test the fail scenarios
        // Test processing fail
        Mockito.when(priceService.calculateForUnits(1, 3))
                .thenThrow(new ServiceException(ServiceException.PROCESSING_FAILED, "Error while fetching data"));

        mockMvc.perform(get(BASE_URI.concat("/products/1/prices?noOfUnits=3")))
                .andExpect(status().isInternalServerError()) // Check for internal server error(500)
                .andExpect(jsonPath("$.status", Matchers.is(0))) // Check for fail status
                .andExpect(jsonPath("$.errorMsg",
                        Matchers.is("Error while fetching data"))); // Check for error message

        // Test validation fail
        Mockito.when(priceService.calculateForUnits(1, 2))
                .thenThrow(new ServiceException(ServiceException.VALIDATION_FAILED,
                        "label.web.store.val.error.no.carton.available"));
        Mockito.when(messageSourceUtil.getMessage("label.web.store.val.error.no.carton.available", new Object[0]))
                .thenReturn("No carton(s) available for the product");

        mockMvc.perform(get(BASE_URI.concat("/products/1/prices?noOfUnits=2")))
                .andExpect(status().isOk()) // Check for request success(200)
                .andExpect(jsonPath("$.status", Matchers.is(0))) // Check for fail status
                .andExpect(jsonPath("$.errorMsg",
                        Matchers.is("No carton(s) available for the product"))); // Check for error message

        // Test product not found
        Mockito.when(productService.isExist(3)).thenReturn(false);

        mockMvc.perform(get(BASE_URI.concat("/products/3/prices?noOfUnits=4")))
                .andExpect(status().isNotFound()); // Check for result not found(404)
    }

    @Test
    @Order(5)
    public void testGetCartonsPrice() throws Exception {
        // Test the success scenario
        Mockito.when(cartonService.isExist(1)).thenReturn(true);
        Mockito.when(priceService.calculateForCartons(1, 2)).thenReturn(BigDecimal.valueOf(350.00));

        mockMvc.perform(get(BASE_URI.concat("/products/cartons/1/prices?noOfCartons=2")))
                .andExpect(status().isOk()) // Check for request success(200)
                .andExpect(jsonPath("$.status", Matchers.is(1))) // Check for success status
                .andExpect(jsonPath("$.result", Matchers.is(350.00))); // Check for price as "350.00"

        // Test the fail scenarios
        // Test processing fail
        Mockito.when(priceService.calculateForCartons(1, 3))
                .thenThrow(new ServiceException(ServiceException.PROCESSING_FAILED, "Error while fetching data"));

        mockMvc.perform(get(BASE_URI.concat("/products/cartons/1/prices?noOfCartons=3")))
                .andExpect(status().isInternalServerError()) // Check for internal server error(500)
                .andExpect(jsonPath("$.status", Matchers.is(0))) // Check for fail status
                .andExpect(jsonPath("$.errorMsg",
                        Matchers.is("Error while fetching data"))); // Check for error message

        // Test carton not found
        Mockito.when(cartonService.isExist(3)).thenReturn(false);

        mockMvc.perform(get(BASE_URI.concat("/products/cartons/3/prices?noOfCartons=1")))
                .andExpect(status().isNotFound()); // Check for result not found(404)
    }
}
