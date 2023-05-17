package com.tup.textilapp.repository;

import com.tup.textilapp.model.dto.TotalEarningsPerMonthDTO;
import com.tup.textilapp.model.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {
    @Query("SELECT NEW com.tup.textilapp.model.dto.TotalEarningsPerMonthDTO(YEAR(p.date), MONTH(p.date), SUM(p.ammountCharged)) " +
            "FROM PaymentEntity p " +
            "WHERE p.date BETWEEN ?1 AND ?2 " +
            "GROUP BY YEAR(p.date), MONTH(p.date)")
    List<TotalEarningsPerMonthDTO> getTotalEarningsPerMonth(Date startDate, Date endDate);
}
