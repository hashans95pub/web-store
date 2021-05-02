package com.my.webstore.service.impl;

import com.my.webstore.enums.UnitPriceCalcFrom;
import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.ProductCarton;
import com.my.webstore.repo.ProductCartonRepository;
import com.my.webstore.repo.ProductRepository;
import com.my.webstore.service.PriceService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The service will provide price calculation related functionalities
 * <p>
 * calculateForUnits(int productId, short noOfUnits): calculate price for unit(s) of the given product
 * calculateForCartons(int cartonId, int noOfCartons): calculate price for carton(s) of the given product carton
 */
@Log4j2
@Service
public class PriceServiceImpl extends BaseService implements PriceService {
    private static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.HALF_UP;
    private static final int DEFAULT_SCALE = 2;

    @Autowired
    private ProductCartonRepository productCartonRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${web.store.unit.price.calculate.from:MAX}")
    private UnitPriceCalcFrom unitPriceCalcFrom;

    @Value("${web.store.manual.labor.compensate.percentage:30}")
    private short laborCompensatePercentage;

    /**
     * The method finds the suitable carton to be used for the unit price calculation based on the configuration
     *
     * @param unitPriceCalcFrom whether which carton to be used for the calculation,
     *                          the carton having max number of units or min number of units
     * @param cartons           list of existing cartons for a product
     * @return the carton to be used for the calculation
     */
    private ProductCarton getCartonForCalc(final UnitPriceCalcFrom unitPriceCalcFrom, final List<ProductCarton> cartons) {
        ProductCarton cartonToReturn = cartons.get(0);
        for (ProductCarton carton : cartons) {
            if (unitPriceCalcFrom == UnitPriceCalcFrom.MIN && cartonToReturn.getNoOfUnits() > carton.getNoOfUnits()) {
                cartonToReturn = carton;
            } else if (unitPriceCalcFrom == UnitPriceCalcFrom.MAX && cartonToReturn.getNoOfUnits() < carton.getNoOfUnits()) {
                cartonToReturn = carton;
            }
        }
        return cartonToReturn;
    }

    /**
     * The method will scale(default:2) and round(default:HALF_UP) the decimal value
     *
     * @param value A floating point value
     * @return scaled and rounded floating point value
     */
    private BigDecimal round(BigDecimal value) {
        log.debug("value:{}", value);
        return value.setScale(DEFAULT_SCALE, DEFAULT_ROUND_MODE);
    }


    /**
     * The method will calculate price for cartons
     *
     * @param cartonPrice price of a carton
     * @param noOfCartons number of cartons
     * @return price for cartons
     */
    private BigDecimal calcCartonsPrice(final BigDecimal cartonPrice, int noOfCartons) {
        return cartonPrice.multiply(BigDecimal.valueOf(noOfCartons));
    }

    @Override
    public BigDecimal calculateForUnits(final int productId, int noOfUnits) throws ServiceException {
        log.info("@calculateForUnits(productId:{}, noOfUnits:{})...", productId, noOfUnits);
        try {
            // Get all cartons for the product
            final List<ProductCarton> cartons = productCartonRepository.findAllByProductId(productId);
            log.debug("cartons:{}", cartons);

            if (CollectionUtils.isNotEmpty(cartons)) {
                // Sort carton list by no of units in descending order to optimize the calculation
                cartons.sort(Comparator.comparing(ProductCarton::getNoOfUnits).reversed());
                log.debug("cartons.sort():{}", cartons);

                BigDecimal price = BigDecimal.ZERO;
                // Add carton price(s)
                int noOfCartons;
                for (ProductCarton carton : cartons) {
                    log.debug("noOfUnits:{} | carton:{}", noOfUnits, carton);

                    // Add only if the "noOfUnits" having more than or equals to carton's
                    if (noOfUnits >= carton.getNoOfUnits()) {
                        // Calculate number of cartons
                        noOfCartons = noOfUnits / carton.getNoOfUnits();
                        log.debug("noOfCartons:{}", noOfCartons);

                        // Update price by adding carton(s) price
                        price = price.add(calcCartonsPrice(carton.getPrice(), noOfCartons));
                        log.debug("price:{}", price);

                        // Update the "noOfUnits" with rest units for next iteration
                        noOfUnits = noOfUnits % carton.getNoOfUnits();
                    }
                }

                // Calculate price for rest of unit(s) if available
                if (noOfUnits > 0) {
                    // Get the carton for unit price calculation, if there are multiple carton then select one
                    // with configuration otherwise get the existing one.
                    ProductCarton cartonForCalc;
                    if (cartons.size() > 1) {
                        cartonForCalc = getCartonForCalc(unitPriceCalcFrom, cartons);
                    } else {
                        cartonForCalc = cartons.get(0);
                    }
                    log.debug("cartonForCalc:{}", cartonForCalc);

                    // Calculate mean unit price, scaled to 2 deciamal places with rounding half up
                    BigDecimal unitPrice = cartonForCalc.getPrice().divide(BigDecimal.valueOf(cartonForCalc.getNoOfUnits()),
                            DEFAULT_SCALE, DEFAULT_ROUND_MODE);
                    log.debug("mean unitPrice:{}", unitPrice);

                    // Add and round labor compensate to the mean price
                    unitPrice = round(unitPrice.multiply(BigDecimal.valueOf(1 + (laborCompensatePercentage / (float) 100))));
                    log.debug("final unitPrice:{}", unitPrice);

                    // Add prices for rest of units
                    price = price.add(unitPrice.multiply(BigDecimal.valueOf(noOfUnits)));
                }

                return price;
            } else {
                throw new ServiceException(ServiceException.VALIDATION_FAILED,
                        "label.web.store.val.error.no.carton.available");
            }
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }

    @Override
    public BigDecimal calculateForCartons(int cartonId, int noOfCartons) throws ServiceException {
        log.info("@calculateForCartons(cartonId:{}, noOfCartons:{})...", cartonId, noOfCartons);
        try {
            // Get the carton for given id
            final Optional<ProductCarton> cartonOptional = productCartonRepository.findById(cartonId);
            // Check whether carton is exist or not
            if (cartonOptional.isPresent()) {
                // Calculate cartons price
                return calcCartonsPrice(cartonOptional.get().getPrice(), noOfCartons);
            } else {
                throw new ServiceException(ServiceException.VALIDATION_FAILED,
                        "label.web.store.val.error.no.carton.found");
            }
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }
}