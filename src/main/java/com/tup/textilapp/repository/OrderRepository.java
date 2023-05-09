package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.Order;
import com.tup.textilapp.model.entity.OrderState;
import com.tup.textilapp.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserEntity(UserEntity userEntity);
    List<Order> findAllByState(OrderState state);
    List<Order> findAllByStateIn(List<OrderState> states);

}