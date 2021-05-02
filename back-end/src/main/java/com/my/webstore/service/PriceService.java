package com.my.webstore.service;

import com.my.webstore.exception.ServiceException;

import java.math.BigDecimal;

public interface PriceService {
    /**
     * The method calculates the price for unit(s) of the given product.
     *
     * @param productId id of the product
     * @param noOfUnits unit count of the product
     * @return the price for unit(s) of the product
     * @throws ServiceException if there is a processing error or validation error
     */
    BigDecimal calculateForUnits(final int productId, int noOfUnits) throws ServiceException;

    /**
     * The method calculates the price for carton(s) of the given product carton.
     *
     * @param cartonId    id of the product carton
     * @param noOfCartons carton count of the product
     * @return the price for carton(s) of the product
     * @throws ServiceException if there is a processing error or validation error
     */
    BigDecimal calculateForCartons(final int cartonId, int noOfCartons) throws ServiceException;
}
