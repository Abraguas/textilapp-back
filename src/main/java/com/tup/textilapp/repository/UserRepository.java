package com.tup.textilapp.repository;

import com.tup.textilapp.model.dto.UserRankingDTO;
import com.tup.textilapp.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
            "GROUP BY u.id " +
            "ORDER BY SUM(od.quantity * od.pricePerUnit) DESC " +
            "LIMIT 100")
    List<UserRankingDTO> getUsersTotalMoneySpent();
    List<UserEntity> findAllByUsernameContainingIgnoreCase(String infix);
}

