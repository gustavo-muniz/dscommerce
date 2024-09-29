package com.munizdev.dscommerce.repositories;

import com.munizdev.dscommerce.entities.Product;
import com.munizdev.dscommerce.projections.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM (
            SELECT DISTINCT pr.id, pr.name
            FROM tb_product pr
            INNER JOIN tb_product_category pc ON pr.id = pc.product_id
            WHERE (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
            AND LOWER(pr.name) LIKE LOWER(CONCAT('%', :name, '%'))
            ) AS tb_result
            """, countQuery = """
            SELECT COUNT(*) FROM (
            SELECT DISTINCT pr.id, pr.name
            FROM tb_product pr
            INNER JOIN tb_product_category pc ON pr.id = pc.product_id
            WHERE (:categoryIds IS NULL OR pc.category_id IN :categoryIds)
            AND LOWER(pr.name) LIKE LOWER(CONCAT('%', :name, '%'))
            ) as tb_result
            """)
    Page<ProductProjection> searchProducts(List<Long> categoryIds, String name, Pageable pageable);

    @Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productIds")
    List<Product> searchProductsWithCategories(List<Long> productIds);
}
