package com.example.VisitorMicroservice.model;

import com.example.core.enums.VisitorStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Visitor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID residentId;
    private String visitorName;
    private String visitorPhone;
    private String visitorEmail;
    private String purpose;
    private LocalDate visitDate;

    @Enumerated(EnumType.STRING)
    private VisitorStatus status;

}
