package com.my.webstore.controller.v1;

import com.my.webstore.controller.BaseController;
import com.my.webstore.controller.dto.Response;
import com.my.webstore.dto.PriceDto;
import com.my.webstore.dto.ProductCartonDto;
import com.my.webstore.dto.ProductDto;
import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.Product;
import com.my.webstore.model.ProductCarton;
import com.my.webstore.service.PriceService;
import com.my.webstore.service.ProductCartonService;
import com.my.webstore.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/v1")
public class WebStoreController extends BaseController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCartonService cartonService;

    @Autowired
    private PriceService priceService;

    /**
     * The service to fetch all products
     *
     * @return List of products
     */
    @GetMapping("/products")
    public ResponseEntity<Response<List<ProductDto>>> getProducts() {
        log.info("@getProducts...");

        final Response<List<ProductDto>> response = new Response<>();
        try {
            final List<ProductDto> products = new ArrayList<>();
            // Fetch & convert all products
            for (Product product : productService.findAll()) {
                log.debug("product:{}", product);
                products.add(new ProductDto(product.getId(), product.getName()));
            }
            log.debug("products:{}", products);

            response.setResultWithSuccessStatus(products);
        } catch (ServiceException e) {
            setErrors(e, response);
            // Response with http status 500 if there is a processing fail
            if (e.getCode() == ServiceException.PROCESSING_FAILED) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // Response with http status 200 If there is a validation error or no errors
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * The service to fetch the price list of a particular product for different no of units
     *
     * @return List of prices for product units
     */
    @GetMapping("/products/{productId}/price-list")
    public ResponseEntity<Response<List<PriceDto>>> getPriceList(
            @PathVariable(name = "productId") final int productId,
            @RequestParam(name = "for", required = false, defaultValue = "50") final short noOfUnits) {
        log.info("@getPriceList(productId:{}, noOfUnits:{})...", productId, noOfUnits);

        final Response<List<PriceDto>> response = new Response<>();

        try {
            // Check the product exists for given id
            if (productService.isExist(productId)) {
                final List<PriceDto> priceList = new ArrayList<>();
                // Calculate price list
                for (int i = 1; i <= noOfUnits; i++) {
                    priceList.add(new PriceDto(i, priceService.calculateForUnits(productId, i)));
                }
                log.debug("priceList:{}", priceList);

                response.setResultWithSuccessStatus(priceList);
            } else {
                // Response with http status 404 If there is no product for the given id
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            setErrors(e, response);
            // Response with http status 500 if there is a processing fail
            if (e.getCode() == ServiceException.PROCESSING_FAILED) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // Response with http status 200 If there is a validation error or no errors
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * The service to fetch carton list of a particular product
     *
     * @return List of cartons
     */
    @GetMapping("/products/{productId}/cartons")
    public ResponseEntity<Response<List<ProductCartonDto>>> getProductCartons(
            @PathVariable(name = "productId") final int productId) {
        log.info("@getProductCartons(productId:{})...", productId);

        final Response<List<ProductCartonDto>> response = new Response<>();
        try {
            // Check the product exists for given id
            if (productService.isExist(productId)) {
                final List<ProductCartonDto> cartons = new ArrayList<>();
                for (ProductCarton carton : cartonService.findAllByProductId(productId)) {
                    log.debug("carton:{}", carton);
                    cartons.add(new ProductCartonDto(carton.getId(), carton.getNoOfUnits(), carton.getPrice()));
                }
                log.debug("cartons:{}", cartons);

                response.setResultWithSuccessStatus(cartons);
            } else {
                // Response with http status 404 If there is no product for the given id
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            setErrors(e, response);
            // Response with http status 500 if there is a processing fail
            if (e.getCode() == ServiceException.PROCESSING_FAILED) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // Response with http status 200 If there is a validation error or no errors
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * The service to get price for units of a particular product
     *
     * @return price for units
     */
    @GetMapping("/products/{productId}/prices")
    public ResponseEntity<Response<BigDecimal>> getUnitsPrice(@PathVariable(name = "productId") final int productId,
                                                              @RequestParam(name = "noOfUnits") final short noOfUnits) {
        log.info("@getUnitsPrice(productId:{}, noOfUnits:{})...", productId, noOfUnits);

        final Response<BigDecimal> response = new Response<>();
        try {
            // Check the product exists for given id
            if (productService.isExist(productId)) {
                // Calculate price for unit(s)
                final BigDecimal price = priceService.calculateForUnits(productId, noOfUnits);
                log.debug("price:{}", price);

                response.setResultWithSuccessStatus(price);
            } else {
                // Response with http status 404 If there is no product for the given id
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            setErrors(e, response);
            // Response with http status 500 if there is a processing fail
            if (e.getCode() == ServiceException.PROCESSING_FAILED) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // Response with http status 200 If there is a validation error or no errors
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * The service to get price for cartons of a particular product
     *
     * @return price for cartons
     */
    @GetMapping("/products/cartons/{cartonId}/prices")
    public ResponseEntity<Response<BigDecimal>> getCartonsPrice(@PathVariable(name = "cartonId") final int cartonId,
                                                                @RequestParam(name = "noOfCartons") final short noOfCartons) {
        log.info("@getCartonsPrice(cartonId:{}, noOfUnits:{})...", cartonId, noOfCartons);

        final Response<BigDecimal> response = new Response<>();
        try {
            // Check the carton exists for given id
            if (cartonService.isExist(cartonId)) {
                // Calculate price for carton(s)
                final BigDecimal price = priceService.calculateForCartons(cartonId, noOfCartons);
                log.debug("price:{}", price);

                response.setResultWithSuccessStatus(price);
            } else {
                // Response with http status 404 If there is no carton for the given id
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (ServiceException e) {
            setErrors(e, response);
            // Response with http status 500 if there is a processing fail
            if (e.getCode() == ServiceException.PROCESSING_FAILED) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        // Response with http status 200 If there is a validation error or no errors
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
