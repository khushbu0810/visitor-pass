package com.example.PassMicroservice.controller;

import com.example.PassMicroservice.config.security.UserPrinciple;
import com.example.PassMicroservice.model.Pass;
import com.example.PassMicroservice.service.PassService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pass")
public class PassController {

    private final PassService passService;

    public PassController(PassService passService) {
        this.passService = passService;
    }

    @PostMapping("/generate/{visitorId}")
    public ResponseEntity<Pass> generatePass(
            @AuthenticationPrincipal UserPrinciple user,
            @PathVariable UUID visitorId) {

        System.out.println("========== PASS CONTROLLER ==========");
        System.out.println("Resident Id : " + user.getUserId());
        System.out.println("Resident Email : " + user.getUsername());
        System.out.println("Visitor Id : " + visitorId);
        System.out.println("====================================");

        Pass pass = passService.generatePassForVisitor(user.getUserId(), visitorId);

        return ResponseEntity.ok(pass);
    }

    @GetMapping
    public ResponseEntity<List<Pass>> getAllPasses() {
        return ResponseEntity.ok(passService.getAllPasses());
    }

    @GetMapping("/{passId}")
    public ResponseEntity<Pass> getPassById(@PathVariable UUID passId) {
        return passService.getPassById(passId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/visitor/{visitorId}")
    public ResponseEntity<Pass> getPassForVisitor(@PathVariable UUID visitorId) {
        return passService.getPassForVisitor(visitorId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/verify/{qrCode}")
    public ResponseEntity<Pass> verifyPass(@PathVariable String qrCode) {
        return ResponseEntity.ok(
                passService.verifyPass(qrCode)
        );
    }

    @PutMapping("/use/{passId}")
    public ResponseEntity<Pass> markPassUsed(@PathVariable UUID passId) {

        return ResponseEntity.ok(
                passService.markPassUsed(passId)
        );
    }

    @PutMapping("/cancel/{passId}")
    public ResponseEntity<Pass> cancelPass(@PathVariable UUID passId) {
        return passService.cancelPass(passId)
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/expire/{passId}")
    public ResponseEntity<String> expirePass(@PathVariable UUID passId) {

        passService.expirePass(passId);

        return ResponseEntity.ok("Pass expired successfully.");
    }

    @DeleteMapping("/{passId}")
    public ResponseEntity<String> deletePass(@PathVariable UUID passId) {

        boolean deleted = passService.deletePass(passId);

        if (deleted) {
            return ResponseEntity.ok("Pass deleted successfully.");
        }

        return ResponseEntity.badRequest().body("Pass not found.");
    }
}