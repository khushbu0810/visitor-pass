package com.example.PassMicroservice.utils;

import com.example.core.enums.VisitorStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class VisitorDTO {
    private UUID id;
    private String visitorName;
    private LocalDate visitDate;
    private VisitorStatus status;
}
