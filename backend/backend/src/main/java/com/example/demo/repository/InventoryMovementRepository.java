package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.InventoryMovement;

import java.util.Optional;

@Repository
public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long>{
    Optional<InventoryMovement> findByDocumentItemId(Long documentItemId);
}
