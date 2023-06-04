package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.Product;
import com.tup.textilapp.model.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement,Integer> {
    List<StockMovement> findAllByProductOrderByDate(Product product);
    List<StockMovement> findByDateBetweenAndProduct(Date startDate, Date endDate, Product product);

}
