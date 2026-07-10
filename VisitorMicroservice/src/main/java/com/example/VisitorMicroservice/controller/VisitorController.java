package com.example.VisitorMicroservice.controller;

import com.example.VisitorMicroservice.config.security.UserPrinciple;
import com.example.VisitorMicroservice.model.Visitor;
import com.example.VisitorMicroservice.service.VisitorService;
import com.example.core.enums.VisitorStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/visitor")
public class VisitorController {
    private final VisitorService visitorService;

    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    //accessing logged in resident
    @PostMapping
    public ResponseEntity<Visitor> addVisitor(@AuthenticationPrincipal UserPrinciple user, @RequestBody Visitor visitor) {
        System.out.println("========== CONTROLLER ==========");
        System.out.println("Resident creating visitor:");
        System.out.println("Resident Id : " + user.getUserId());
        System.out.println("Resident Email : " + user.getUsername());

        System.out.println("Visitor Name : " + visitor.getVisitorName());
        System.out.println("Visitor Phone: " + visitor.getVisitorPhone());
        System.out.println("===============================");
        Visitor visitor1 = visitorService.createVisitor(user.getUserId(), visitor);
        if (visitor1 != null) {
            return ResponseEntity.status(200).body(visitor1);
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/{visitorId}")
    public ResponseEntity<Visitor> getVisitorById(@PathVariable UUID visitorId) {
        Visitor visitor1 = visitorService.getVisitorById(visitorId);
        return ResponseEntity.status(200).body(visitor1);
    }

    @PutMapping("/{visitorId}")
    public ResponseEntity<Visitor> updateVisitor(@PathVariable UUID visitorId, @RequestBody Visitor visitor) {
        Visitor visitor1 = visitorService.updateVisitor(visitorId, visitor);
        if (visitor1 != null) {
            return ResponseEntity.status(200).body(visitor1);
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping
    public ResponseEntity<List<Visitor>> getAllVisitors() {
        List<Visitor> visitor1 = visitorService.getAllVisitors();
        if (visitor1 != null) {
            return ResponseEntity.status(200).body(visitor1);
        }
        return ResponseEntity.status(400).build();
    }

    @DeleteMapping("/{visitorId}")
    public ResponseEntity<String> deleteVisitor(@PathVariable UUID visitorId) {
        Boolean visitor1 = visitorService.deleteVisitor(visitorId);
        return ResponseEntity.status(200).body("Deleted");
    }

    @GetMapping("/resident/my-visitors")
    public ResponseEntity<List<Visitor>> getVisitorsByResident(@AuthenticationPrincipal UserPrinciple user) {
        List<Visitor> visitor1 = visitorService.getAllVisitorsCreatedByResident(user.getUserId());
        if (visitor1 != null) {
            return ResponseEntity.status(200).body(visitor1);
        }
        return ResponseEntity.status(400).build();
    }

    @GetMapping("/today")
    public ResponseEntity<List<Visitor>> getTodayVisitors() {
        List<Visitor> visitors = visitorService.getTodayVisitors();
        return ResponseEntity.ok(visitors);
    }

    @PutMapping("/{visitorId}/{status}")
    public ResponseEntity<Visitor> updateVisitorStatus(
            @PathVariable UUID visitorId,
            @PathVariable VisitorStatus status) {

        Visitor visitor = visitorService.updateVisitorStatus(visitorId, status);
        return ResponseEntity.ok(visitor);
    }

    @ManagedOperation(description = "Generate Pass using Kafka")
    @PostMapping("/{visitorId}/generate-pass")
    public ResponseEntity<String> generatePass(@PathVariable UUID visitorId) {
        visitorService.generatePass(visitorId);
        return ResponseEntity.ok("Pass generation request published.");
    }
}
