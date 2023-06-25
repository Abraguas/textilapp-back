package com.tup.textilapp.repository;

import com.tup.textilapp.model.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageDataRepository extends JpaRepository<ImageData,Integer> {
    Optional<ImageData> findByName(String name);
    boolean existsByName(String name);
}
