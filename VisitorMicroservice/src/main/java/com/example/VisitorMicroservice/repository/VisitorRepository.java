package com.example.VisitorMicroservice.repository;
import com.example.VisitorMicroservice.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    List<Visitor> findByResidentId(UUID residentId);
    List<Visitor> findByVisitDate(LocalDate visitDate);
}