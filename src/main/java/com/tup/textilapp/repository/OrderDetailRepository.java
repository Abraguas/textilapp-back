package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.OrderDetail;
import com.tup.textilapp.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByProduct(Product product);
}
