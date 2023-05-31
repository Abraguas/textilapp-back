package com.tup.textilapp.repository;

import com.tup.textilapp.model.dto.UserRankingDTO;
import com.tup.textilapp.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT NEW com.tup.textilapp.model.dto.UserRankingDTO(u.id, u.username, u.name, u.lastname, SUM(od.quantity * od.pricePerUnit)) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "JOIN o.userEntity u " +
            "JOIN o.state s " +
            "WHERE s.name IN ('Cobrado', 'Entregado') " +
            "AND o.date BETWEEN ?1 AND ?2 " +
            "GROUP BY u.id " +
            "ORDER BY SUM(od.quantity * od.pricePerUnit) DESC " +
            "LIMIT 100")
    List<UserRankingDTO> getUsersTotalMoneySpent(Date startDate, Date endDate);
    List<UserEntity> findAllByUsernameContainingIgnoreCase(String infix);
}

