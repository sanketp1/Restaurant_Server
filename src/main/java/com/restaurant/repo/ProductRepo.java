package com.restaurant.repo;

import com.restaurant.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findAllByCategoryIdAndNameContaining(Long categoryId, String title);

    List<Product> findAllByCategoryId(Long categoryId);

}
