package com.example.ResidentMicroservice.service.impl;

import com.example.ResidentMicroservice.model.User;
import com.example.ResidentMicroservice.repository.ResidentRepo;
import com.example.ResidentMicroservice.repository.UserRepo;
import com.example.ResidentMicroservice.service.ResidentService;
import com.example.ResidentMicroservice.model.Resident;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResidentServiceImpl implements ResidentService {
    private final ResidentRepo residentRepo;
    private final UserRepo userRepo;

    public ResidentServiceImpl(ResidentRepo residentRepo, UserRepo userRepo) {
        this.residentRepo = residentRepo;
        this.userRepo = userRepo;
    }

//    @Override
//    public Resident createResident(Resident resident) {
//        if (userRepo.existsByEmail(resident.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }
//        User user = new User();
//        user.setUsername(resident.getFullName());
//        user.setPassword(passwordEncoder.encode(resident.getPhoneNumber()));
//        user.setEmail(resident.getEmail());
//        user.setRole("RESIDENT");
//        user.setAccountStatus(true);
//        User savedUser = userRepo.save(user);
//        resident.setUser(savedUser);
//        resident.setPhoneNumber("PROFILE_PENDING");
//        resident.setTower("PROFILE_PENDING");
//        resident.setFlatNumber("PROFILE_PENDING");
//        return residentRepo.save(resident);
//    }

    @Override
    public Resident getResidentById(UUID residentId) {
        Optional<Resident> optionalResident=residentRepo.findById(residentId);
        return optionalResident.orElse(null);
    }

    @Override
    public Resident updateResident(UUID residentId, Resident newResident) {
        Optional<Resident> optionalResident=residentRepo.findById(residentId);
        if(optionalResident.isPresent()){
            Resident resident=optionalResident.get();
            resident.setFullName(newResident.getFullName());
            resident.setEmail(newResident.getEmail());
            resident.setPhoneNumber(newResident.getPhoneNumber());
            resident.setFlatNumber(newResident.getFlatNumber());
            resident.setTower(newResident.getTower());
            // Update User table
            User user = resident.getUser();
            if (user != null) {
                user.setUsername(newResident.getFullName());
                user.setEmail(newResident.getEmail());
                userRepo.save(user);
            }
            return residentRepo.save(resident);
        }
        return null;
    }

    @Override
    public boolean deleteResident(UUID residentId) {
        Optional<Resident> optionalResident=residentRepo.findById(residentId);
        if(optionalResident.isPresent()){
            Resident resident = optionalResident.get();
            User user = resident.getUser();
            // Delete Resident first
            residentRepo.delete(resident);
            // Delete associated User
            if (user != null) {
                userRepo.delete(user);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Resident> getAllResidents() {
        return residentRepo.findAll();
    }

    @Override
    public Optional<Resident> getResidentProfile(UUID userId) {
        return residentRepo.findByUserUserId(userId);
    }
}
