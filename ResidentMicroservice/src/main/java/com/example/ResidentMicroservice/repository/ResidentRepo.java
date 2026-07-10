package com.example.ResidentMicroservice.repository;

import com.example.ResidentMicroservice.model.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResidentRepo extends JpaRepository<Resident, UUID> {

    Optional<Resident> findByUserUserId(UUID userId);
}
