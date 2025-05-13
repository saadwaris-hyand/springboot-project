import com.example.ticketvalidator.dto.TicketDTO;
import com.example.ticketvalidator.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketServiceTest {

    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketService = new TicketService();
    }

    @Test
    void testValidIncidentTicket() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("INCIDENT");
        ticket.setCreatedDate(LocalDate.now());
        ticket.setPriority("HIGH");
        ticket.setCategory("Database");
        ticket.setImpact("Major");

        Map<String, Object> result = ticketService.validate(ticket);

        assertEquals("VALID", result.get("status"));
        assertEquals("Ticket is valid", result.get("message"));
    }

    @Test
    void testInvalidIncidentTicket_MissingImpact() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("INCIDENT");
        ticket.setCreatedDate(LocalDate.now());
        ticket.setPriority("LOW");
        ticket.setCategory("UI");

        Map<String, Object> result = ticketService.validate(ticket);

        assertEquals("INVALID", result.get("status"));
        List<String> errors = extractErrorList(result.get("errors"));
        assertTrue(errors.contains("INCIDENT: impact is required"));
    }

    @Test
    void testValidChangeRequestTicket() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("CHANGE_REQUEST");
        ticket.setCreatedDate(LocalDate.now());
        ticket.setPriority("MEDIUM");
        ticket.setPlannedExecutionDate(LocalDate.now().plusDays(3));
        ticket.setApprover("Alice");

        Map<String, Object> result = ticketService.validate(ticket);

        assertEquals("VALID", result.get("status"));
    }

    @Test
    void testInvalidChangeRequest_MissingApprover() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("CHANGE_REQUEST");
        ticket.setPlannedExecutionDate(LocalDate.now().plusDays(1));

        Map<String, Object> result = ticketService.validate(ticket);

        assertEquals("INVALID", result.get("status"));
        List<String> errors = extractErrorList(result.get("errors"));
        assertTrue(errors.contains("CHANGE_REQUEST: approver is required"));
    }

    @Test
    void testValidMaintenanceTicket() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("MAINTENANCE");
        ticket.setCreatedDate(LocalDate.now());
        ticket.setPriority("CRITICAL");
        ticket.setMaintenanceWindowStart(LocalDateTime.now().plusHours(1));
        ticket.setMaintenanceWindowEnd(LocalDateTime.now().plusHours(3));
        ticket.setAffectedComponents(List.of("Server1", "DB1"));

        Map<String, Object> result = ticketService.validate(ticket);

        assertEquals("VALID", result.get("status"));
    }

    @Test
    void testInvalidMaintenanceTicket_EndBeforeStart() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("MAINTENANCE");
        ticket.setMaintenanceWindowStart(LocalDateTime.now().plusHours(2));
        ticket.setMaintenanceWindowEnd(LocalDateTime.now().plusHours(1));
        ticket.setAffectedComponents(List.of("DB"));

        Map<String, Object> result = ticketService.validate(ticket);

        assertEquals("INVALID", result.get("status"));
        List<String> errors = extractErrorList(result.get("errors"));
        assertTrue(errors.contains("MAINTENANCE: start time must be before end time"));
    }

    @Test
    void testInvalidTicketType() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("UNKNOWN_TYPE");

        Map<String, Object> result = ticketService.validate(ticket);

        assertEquals("INVALID", result.get("status"));
        List<String> errors = extractErrorList(result.get("errors"));
        assertTrue(errors.contains("Invalid ticket type"));
    }

    @Test
    void testStoreRawResult() {
        TicketDTO ticket = new TicketDTO();
        List<String> annotationErrors = List.of("createdDate is required");

        ticketService.storeRawResult(ticket, annotationErrors);

        List<Map<String, Object>> allTickets = ticketService.getAllTickets();
        assertEquals(1, allTickets.size());
        assertEquals("INVALID", allTickets.get(0).get("status"));
    }

    @Test
    void testGetAllTicketsStoresCorrectly() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("INCIDENT");
        ticket.setSystem("CRM");
        ticket.setCreatedDate(LocalDate.now());
        ticket.setPriority("HIGH");
        ticket.setDescription("Login failure");
        ticket.setResponsible("admin");
        ticket.setCategory("Network");
        ticket.setImpact("High");

        Map<String, Object> result = ticketService.validate(ticket);

        if ("VALID".equals(result.get("status"))) {
            ticketService.storeValidResult(ticket);
        } else {
            List<String> errors = extractErrorList(result.get("errors"));
            ticketService.storeRawResult(ticket, errors);
        }

        List<Map<String, Object>> allTickets = ticketService.getAllTickets();
        assertEquals(1, allTickets.size());
        assertTrue(allTickets.get(0).containsKey("ticket"));
    }

    @SuppressWarnings("unchecked")
    private List<String> extractErrorList(Object obj) {
        if (obj instanceof List<?>) {
            return (List<String>) obj;
        }
        throw new IllegalArgumentException("Expected List<String> but got: " + obj.getClass());
    }

}
