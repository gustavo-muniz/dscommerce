package com.munizdev.dscommerce.factories;

import com.munizdev.dscommerce.dto.ProductDTO;
import com.munizdev.dscommerce.entities.Category;
import com.munizdev.dscommerce.entities.Product;

public class Factory {

    public static Product createProduct() {
        Product product =  new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png");
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product);
    }
}
