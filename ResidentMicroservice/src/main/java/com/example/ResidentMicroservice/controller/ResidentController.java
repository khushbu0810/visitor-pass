package com.example.ResidentMicroservice.controller;

import com.example.ResidentMicroservice.service.ResidentService;
import com.example.ResidentMicroservice.model.Resident;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/residents")
public class ResidentController {
    private final ResidentService residentService;

    public ResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

//    @PostMapping
//    public ResponseEntity<Resident> addResident(@RequestBody Resident resident) {
//        Resident resident1 = residentService.createResident(resident);
//        if (resident1 != null) {
//            return ResponseEntity.status(200).body(resident1);
//        }
//        return ResponseEntity.status(400).build();
//    }

    @GetMapping("/{residentId}")
    public ResponseEntity<Resident> getResidentById(@PathVariable UUID residentId) {
        Resident resident1 = residentService.getResidentById(residentId);
        return ResponseEntity.status(200).body(resident1);
    }

    @PutMapping("/{residentId}")
    public ResponseEntity<Resident> updateResident(@PathVariable UUID residentId,@RequestBody Resident resident) {
        Resident resident1 = residentService.updateResident(residentId,resident);
        if (resident1 != null) {
            return ResponseEntity.status(200).body(resident1);
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping
    public ResponseEntity<List<Resident>> getAllResidents() {
        List<Resident> resident1 = residentService.getAllResidents();
        if (resident1 != null) {
            return ResponseEntity.status(200).body(resident1);
        }
        return ResponseEntity.status(400).build();
    }

    @DeleteMapping("/{residentId}")
    public ResponseEntity<String> deleteResident(@PathVariable UUID residentId) {
        Boolean resident1 = residentService.deleteResident(residentId);
        return ResponseEntity.status(200).body("Deleted");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Optional<Resident>> getResidentProfile(@PathVariable UUID userId){
        Optional<Resident> resident = residentService.getResidentProfile(userId);
        return ResponseEntity.ok(resident);
    }
}
