package com.munizdev.dscommerce.services;

import com.munizdev.dscommerce.repositories.ProductRepository;
import com.munizdev.dscommerce.dto.ProductDTO;
import com.munizdev.dscommerce.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> result = repository.findById(id);

        return new ProductDTO(result.get());
    }
}
