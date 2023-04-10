package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStateRepository extends JpaRepository<OrderState, Integer> {
    OrderState findByName(String name);
}