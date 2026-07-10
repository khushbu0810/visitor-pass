package com.example.PassMicroservice.model;

import com.example.core.enums.PassStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pass {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID passId;
    private UUID visitorId;
    private UUID residentId;
    private String qrCode;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String qrImage;

    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    private PassStatus status;
}
