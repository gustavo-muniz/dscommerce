package com.munizdev.dscommerce.repositories;

import com.munizdev.dscommerce.entities.Category;
import com.munizdev.dscommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
