package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {

}