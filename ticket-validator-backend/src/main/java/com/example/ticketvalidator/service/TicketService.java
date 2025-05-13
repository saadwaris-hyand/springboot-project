package com.example.ticketvalidator.service;

import com.example.ticketvalidator.dto.TicketDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TicketService {

    private final List<Map<String, Object>> storedTickets = new ArrayList<>();

    public Map<String, Object> validate(TicketDTO ticket) {
        List<String> errors = new ArrayList<>();

        // Common field validation
        if (ticket.getCreatedDate() != null && ticket.getCreatedDate().isAfter(LocalDate.now())) {
            errors.add("createdDate must not be in the future");
        }

        if (ticket.getPriority() != null &&
                !List.of("LOW", "MEDIUM", "HIGH", "CRITICAL").contains(ticket.getPriority().toUpperCase())) {
            errors.add("Invalid priority value");
        }

        // Type-specific validation
        String type = ticket.getType().toUpperCase();
        switch (type) {
            case "INCIDENT" -> {
                if (ticket.getCategory() == null ||
                        !List.of("Network", "Database", "UI", "Access Issue").contains(ticket.getCategory())) {
                    errors.add("INCIDENT: category must be one of Network, Database, UI, Access Issue");
                }
                if (ticket.getImpact() == null || ticket.getImpact().isBlank()) {
                    errors.add("INCIDENT: impact is required");
                }
            }
            case "CHANGE_REQUEST" -> {
                if (ticket.getPlannedExecutionDate() == null ||
                        !ticket.getPlannedExecutionDate().isAfter(LocalDate.now())) {
                    errors.add("CHANGE_REQUEST: planned execution date must be in the future");
                }
                if (ticket.getApprover() == null || ticket.getApprover().isBlank()) {
                    errors.add("CHANGE_REQUEST: approver is required");
                }
            }
            case "MAINTENANCE" -> {
                if (ticket.getMaintenanceWindowStart() == null || ticket.getMaintenanceWindowEnd() == null) {
                    errors.add("MAINTENANCE: maintenance window start/end times are required");
                } else {
                    if (!ticket.getMaintenanceWindowStart().isBefore(ticket.getMaintenanceWindowEnd())) {
                        errors.add("MAINTENANCE: start time must be before end time");
                    }
                    if (!ticket.getMaintenanceWindowStart().isAfter(LocalDateTime.now()) ||
                            !ticket.getMaintenanceWindowEnd().isAfter(LocalDateTime.now())) {
                        errors.add("MAINTENANCE: both start and end must be in the future");
                    }
                }
                if (ticket.getAffectedComponents() == null || ticket.getAffectedComponents().isEmpty()) {
                    errors.add("MAINTENANCE: affected components must not be null");
                }
            }
            default -> errors.add("Invalid ticket type");
        }

        Map<String, Object> response = new HashMap<>();
        if (errors.isEmpty()) {
            response.put("status", "VALID");
            response.put("message", "Ticket is valid");
        } else {
            response.put("status", "INVALID");
            response.put("errors", errors);
        }

        return response;
    }

    public void storeValidResult(TicketDTO ticket) {
        storedTickets.add(Map.of(
                "ticket", ticket,
                "status", "VALID"
        ));
    }

    public void storeRawResult(TicketDTO ticket, List<String> annotationErrors) {
        storedTickets.add(Map.of(
                "ticket", ticket,
                "status", "INVALID",
                "errors", annotationErrors
        ));
    }

    public List<Map<String, Object>> getAllTickets() {
        return storedTickets;
    }
}
