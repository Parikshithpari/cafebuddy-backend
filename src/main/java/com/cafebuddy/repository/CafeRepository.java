package com.cafebuddy.repository;

import com.cafebuddy.model.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long> {
    List<Cafe> findByNameContainingIgnoreCaseOrAreaContainingIgnoreCase(String name, String area);
}
