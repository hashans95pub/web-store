package com.my.webstore.service;

import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.Product;

import java.util.List;

public interface ProductService {
    /**
     * The method fetches all available products
     *
     * @return list of products
     * @throws ServiceException if there is a processing error or validation error
     */
    List<Product> findAll() throws ServiceException;

    /**
     * The method checks the product exists for the given id
     *
     * @param id id of the product
     * @return {@literal true} if the product exists, {@literal false} otherwise
     * @throws ServiceException if there is a processing error or validation error
     */
    boolean isExist(int id) throws ServiceException;
}
