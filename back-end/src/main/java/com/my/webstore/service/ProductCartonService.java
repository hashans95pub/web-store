package com.my.webstore.service;

import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.ProductCarton;

import java.util.List;

public interface ProductCartonService {
    /**
     * The method fetches all available cartons of the given product
     *
     * @param productId id of the product
     * @return list of product's cartons
     * @throws ServiceException if there is a processing error or validation error
     */
    List<ProductCarton> findAllByProductId(int productId) throws ServiceException;

    /**
     * The method checks the product carton exists for the given id
     *
     * @param id id of the product carton
     * @return {@literal true} if the carton exists, {@literal false} otherwise
     * @throws ServiceException if there is a processing error or validation error
     */
    boolean isExist(int id) throws ServiceException;
}
