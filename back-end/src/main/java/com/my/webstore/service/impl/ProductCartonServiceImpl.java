package com.my.webstore.service.impl;

import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.ProductCarton;
import com.my.webstore.repo.ProductCartonRepository;
import com.my.webstore.service.ProductCartonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service will provide product cartons related functionalities
 * <p>
 * findAllByProductId(int productId): get all cartons for the given product
 */
@Log4j2
@Service
public class ProductCartonServiceImpl extends BaseService implements ProductCartonService {
    @Autowired
    private ProductCartonRepository repository;

    @Override
    public List<ProductCarton> findAllByProductId(int productId) throws ServiceException {
        try {
            return repository.findAllByProductId(productId);
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }

    @Override
    public boolean isExist(int id) throws ServiceException {
        try {
            return repository.existsById(id);
        } catch (DataAccessException e) {
            throw translateException(e);
        }
    }
}
