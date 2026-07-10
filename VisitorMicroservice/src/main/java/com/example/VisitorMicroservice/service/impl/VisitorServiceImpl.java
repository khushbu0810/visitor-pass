package com.example.VisitorMicroservice.service.impl;

import com.example.VisitorMicroservice.model.Visitor;
import com.example.VisitorMicroservice.producer.PassProducer;
import com.example.VisitorMicroservice.repository.VisitorRepository;
import com.example.VisitorMicroservice.service.VisitorService;
import com.example.core.enums.VisitorStatus;
import com.example.core.events.PassCreatedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VisitorServiceImpl implements VisitorService {
    private final PassProducer passProducer;
    private final VisitorRepository visitorRepository;

    public VisitorServiceImpl(PassProducer passProducer, VisitorRepository visitorRepository) {
        this.passProducer = passProducer;
        this.visitorRepository = visitorRepository;
    }

    @Override
    public Visitor createVisitor(UUID residentId, Visitor visitor) {
        visitor.setResidentId(residentId);
        visitor.setStatus(VisitorStatus.PENDING);
        return visitorRepository.save(visitor);
    }

    @Override
    public Visitor getVisitorById(UUID visitorId) {
        Optional<Visitor>optVisitor=visitorRepository.findById(visitorId);
        return optVisitor.orElse(null);
    }

    @Override
    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    @Override
    public List<Visitor> getAllVisitorsCreatedByResident(UUID residentId) {
        return visitorRepository.findByResidentId(residentId);
    }

    @Override
    public Visitor updateVisitor(UUID visitorId, Visitor newVisitor) {
        Optional<Visitor>optVisitor=visitorRepository.findById(visitorId);
        if(optVisitor.isPresent()){
            Visitor visitor=optVisitor.get();
            visitor.setVisitorName(newVisitor.getVisitorName());
            visitor.setVisitorEmail(newVisitor.getVisitorEmail());
            visitor.setVisitorPhone(newVisitor.getVisitorPhone());
            visitor.setPurpose(newVisitor.getPurpose());
            visitor.setVisitDate(newVisitor.getVisitDate());
            return visitorRepository.save(visitor);
        }
        return null;
    }

    @Override
    public boolean deleteVisitor(UUID visitorId) {
        Optional<Visitor>optVisitor=visitorRepository.findById(visitorId);
        if(optVisitor.isPresent()){
            visitorRepository.delete(optVisitor.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Visitor> getTodayVisitors() {
        return visitorRepository.findByVisitDate(LocalDate.now());
    }

    @Override
    public Visitor updateVisitorStatus(UUID visitorId, VisitorStatus status) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));
        VisitorStatus current = visitor.getStatus();
        switch (status) {
            case APPROVED -> {
                if (current != VisitorStatus.PENDING) {
                    throw new RuntimeException("Only PENDING visitors can be approved.");
                }
            }

            case CANCELLED -> {
                if (current == VisitorStatus.ENTERED || current == VisitorStatus.EXITED) {
                    throw new RuntimeException("Cannot cancel after visitor has entered.");
                }
            }

            case EXPIRED -> {
                if (current != VisitorStatus.PENDING) {
                    throw new RuntimeException("Only PENDING visitors can expire.");
                }
            }

            case ENTERED -> {
                if (current != VisitorStatus.APPROVED) {
                    throw new RuntimeException("Visitor must be APPROVED before entering.");
                }
            }

            case EXITED -> {
                if (current != VisitorStatus.ENTERED) {
                    throw new RuntimeException("Visitor must ENTER before exiting.");
                }
            }

            case PENDING -> {
                throw new RuntimeException("Cannot change back to PENDING.");
            }
        }

        visitor.setStatus(status);
        return visitorRepository.save(visitor);
    }

    @Override
    public void generatePass(UUID visitorId) {
        Visitor visitor = visitorRepository.findById(visitorId)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));

        PassCreatedEvent event =
                new PassCreatedEvent(
                        visitor.getResidentId(),
                        visitor.getId());
        passProducer.publishVisitorCreated(event);

    }
}
