package com.my.webstore.repo;

import com.my.webstore.model.ProductCarton;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCartonRepository extends JpaRepository<ProductCarton, Integer> {
    List<ProductCarton> findAllByProductId(int productId) throws DataAccessException;
}
