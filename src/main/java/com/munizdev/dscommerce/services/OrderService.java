package com.munizdev.dscommerce.services;

import com.munizdev.dscommerce.dto.OrderDTO;
import com.munizdev.dscommerce.entities.Order;
import com.munizdev.dscommerce.repositories.OrderRepository;
import com.munizdev.dscommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public OrderDTO findById(Long id) {
        Order order = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
        return new OrderDTO(order);
    }
}
