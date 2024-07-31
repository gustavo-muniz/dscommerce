package com.munizdev.dscommerce.repositories;

import com.munizdev.dscommerce.entities.OrderItem;
import com.munizdev.dscommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
}
