package com.example.VisitorMicroservice.service;

import com.example.VisitorMicroservice.model.Visitor;
import com.example.core.enums.VisitorStatus;

import java.util.List;
import java.util.UUID;

public interface VisitorService {
    Visitor createVisitor(UUID residentId, Visitor visitor);
    Visitor getVisitorById(UUID visitorId);
    List<Visitor> getAllVisitors();
    List<Visitor> getAllVisitorsCreatedByResident(UUID residentId);
    Visitor updateVisitor(UUID visitorId, Visitor newVisitor);
    boolean deleteVisitor(UUID visitorId);
    List<Visitor> getTodayVisitors();
    Visitor updateVisitorStatus(UUID visitorId, VisitorStatus status);
    void generatePass(UUID visitorId);
}
