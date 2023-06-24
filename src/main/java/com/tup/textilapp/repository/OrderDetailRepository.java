package com.tup.textilapp.repository;

import com.tup.textilapp.model.dto.HighestSellingProductsDTO;
import com.tup.textilapp.model.entity.OrderDetail;
import com.tup.textilapp.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByProduct(Product product);

    @Query("SELECT NEW com.tup.textilapp.model.dto.HighestSellingProductsDTO(p.id, p.name, SUM(od.quantity)) " +
            "FROM OrderDetail od " +
            "JOIN od.product p " +
            "JOIN od.order o " +
            "JOIN o.state s " +
            "WHERE o.date BETWEEN ?1 AND ?2 " +
            "AND s.name IN ('Cobrado', 'Entregado') " +
            "GROUP BY p.id " +
            "ORDER BY SUM(od.quantity) DESC " +
            "LIMIT 10")
    List<HighestSellingProductsDTO> highestSellingProducts(Date startDate, Date endDate);
}
