package com.restaurant.repo;

import com.restaurant.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {


    List<Category> findAllByNameContaining(String title);

}
