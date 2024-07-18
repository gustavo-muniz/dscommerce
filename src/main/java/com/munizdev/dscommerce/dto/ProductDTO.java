package com.munizdev.dscommerce.dto;

import com.munizdev.dscommerce.entities.Product;
import jakarta.persistence.Column;

public class ProductDTO {

    private String name;
    private String description;
    private Double price;
    private String imgUrl;

    public ProductDTO() {
    }

    public ProductDTO(Product product) {
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
