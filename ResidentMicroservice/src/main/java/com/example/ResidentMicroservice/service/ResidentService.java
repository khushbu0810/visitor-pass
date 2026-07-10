package com.example.ResidentMicroservice.service;

import com.example.ResidentMicroservice.model.Resident;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResidentService {
//    Resident createResident(Resident resident);
    Resident getResidentById(UUID residentId);
    Resident updateResident(UUID residentId,Resident newResident);
    boolean deleteResident(UUID residentId);
    List<Resident> getAllResidents();
    Optional<Resident> getResidentProfile(UUID userId);
}
