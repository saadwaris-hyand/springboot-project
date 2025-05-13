package com.example.ticketvalidator.controller;

import com.example.ticketvalidator.dto.TicketDTO;
import com.example.ticketvalidator.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.ticketvalidator.service.BulkTicketValidationService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class TicketValidationController {

    private final TicketService ticketService;
    private final BulkTicketValidationService bulkTicketValidationService;
    @Autowired
    public TicketValidationController(TicketService ticketService, BulkTicketValidationService bulkTicketValidationService) {
        this.ticketService = ticketService;
        this.bulkTicketValidationService = bulkTicketValidationService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateTicket(
            @Valid @RequestBody TicketDTO ticket,
            BindingResult result
    ) {
        List<String> allErrors = result.getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());

        // Business validation (does not store)
        Map<String, Object> serviceResponse = ticketService.validate(ticket);

        if (serviceResponse.containsKey("errors")) {
            @SuppressWarnings("unchecked")
            List<String> businessErrors = (List<String>) serviceResponse.get("errors");
            allErrors.addAll(businessErrors);
        }

        if (!allErrors.isEmpty()) {
            ticketService.storeRawResult(ticket, allErrors);
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "failed",
                    "errors", allErrors
            ));
        }

        ticketService.storeValidResult(ticket);
        return ResponseEntity.ok(serviceResponse);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/tickets")
    public ResponseEntity<List<Map<String, Object>>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/validate/bulk")
    public ResponseEntity<List<Map<String, Object>>> validateMultipleTickets(
            @RequestBody List<TicketDTO> tickets) {
        List<Map<String, Object>> response = bulkTicketValidationService.validateMultipleTickets(tickets);
        return ResponseEntity.ok(response);
    }

}