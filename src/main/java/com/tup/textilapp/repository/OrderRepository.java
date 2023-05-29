package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.Order;
import com.tup.textilapp.model.entity.OrderState;
import com.tup.textilapp.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUserEntity(UserEntity userEntity);
    Page<Order> findAllByUserEntity(UserEntity userEntity, Pageable p);
    List<Order> findAllByStateIn(List<OrderState> states);
    Page<Order> findAllByStateIn(List<OrderState> states, Pageable p);
    Page<Order> findAllByUserEntity_UsernameContainingIgnoreCase(String infix, Pageable p);
    Page<Order> findAllByStateInAndUserEntity_UsernameContainingIgnoreCase(List<OrderState> states, String infix, Pageable p);

}