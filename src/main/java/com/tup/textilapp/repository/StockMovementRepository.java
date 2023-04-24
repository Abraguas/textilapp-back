package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.Product;
import com.tup.textilapp.model.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement,Integer> {
    List<StockMovement> findAllByProduct(Product product);
}
