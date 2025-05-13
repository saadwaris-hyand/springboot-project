package com.example.ticketvalidator.service;


import com.example.ticketvalidator.dto.TicketDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BulkTicketValidationService {

    private final TicketService ticketService;
    private final Validator validator;

    @Autowired
    public BulkTicketValidationService(TicketService ticketService, Validator validator) {
        this.ticketService = ticketService;
        this.validator = validator;
    }

    public List<Map<String, Object>> validateMultipleTickets(List<TicketDTO> tickets) {
        List<Map<String, Object>> responses = new ArrayList<>();

        for (TicketDTO ticket : tickets) {
            // Step 1: Annotation-based validation
            Set<ConstraintViolation<TicketDTO>> violations = validator.validate(ticket);
            List<String> annotationErrors = new ArrayList<>();
            for (ConstraintViolation<TicketDTO> violation : violations) {
                annotationErrors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            }

            // Step 2: Business validation
            Map<String, Object> serviceResponse = ticketService.validate(ticket);

            // Step 3: Merge all errors
            List<String> allErrors = new ArrayList<>(annotationErrors);
            if (serviceResponse.containsKey("errors")) {
                @SuppressWarnings("unchecked")
                List<String> businessErrors = (List<String>) serviceResponse.get("errors");
                allErrors.addAll(businessErrors);
            }

            // Step 4: Store once based on status
            if (!allErrors.isEmpty()) {
                ticketService.storeRawResult(ticket, allErrors);
                responses.add(Map.of(
                        "status", "failed",
                        "ticket", ticket,
                        "errors", allErrors
                ));
            } else {
                ticketService.storeValidResult(ticket);
                responses.add(Map.of(
                        "status", "success",
                        "ticket", ticket,
                        "message", "Ticket is valid"
                ));
            }
        }

        return responses;
    }

}
