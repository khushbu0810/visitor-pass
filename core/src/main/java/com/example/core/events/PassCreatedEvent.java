package com.example.core.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassCreatedEvent {
    private UUID residentId;
    private UUID visitorId;
}
