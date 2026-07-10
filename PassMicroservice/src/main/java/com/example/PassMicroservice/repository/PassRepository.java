package com.example.PassMicroservice.repository;

import com.example.PassMicroservice.model.Pass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassRepository extends JpaRepository<Pass, UUID> {
    Optional<Pass> findByQrCode(String qrCode);
    Optional<Pass> findByVisitorId(UUID visitorId);

}
