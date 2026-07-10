package com.example.PassMicroservice.service;

import com.example.PassMicroservice.model.Pass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PassService {

    Pass generatePassForVisitor(UUID residentId, UUID visitorId);

    List<Pass> getAllPasses();

    Optional<Pass> getPassById(UUID passId);

    Optional<Pass> getPassForVisitor(UUID visitorId);

    Pass verifyPass(String qrCode);

    Pass markPassUsed(UUID passId);

    boolean deletePass(UUID passId);

    Optional<Pass> cancelPass(UUID passId);

    void expirePass(UUID passId);
}
