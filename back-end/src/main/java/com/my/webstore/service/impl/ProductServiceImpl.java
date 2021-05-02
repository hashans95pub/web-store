package com.my.webstore.service.impl;

import com.my.webstore.exception.ServiceException;
import com.my.webstore.model.Product;
import com.my.webstore.repo.ProductRepository;
import com.my.webstore.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The service will provide product related functionalities
 * <p>
 * findAll(): get all available products
 */
@Log4j2
@Service
public class ProductServiceImpl extends BaseService implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> findAll() throws ServiceException {
        try {
            return repository.findAll();
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
