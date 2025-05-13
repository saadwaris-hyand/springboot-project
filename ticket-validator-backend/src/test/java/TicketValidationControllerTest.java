import com.example.ticketvalidator.dto.TicketDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.ticketvalidator.TicketValidatorApplication;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TicketValidatorApplication.class)
@AutoConfigureMockMvc
public class TicketValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Test 1: Valid single ticket
    @Test
    void shouldValidateSingleValidIncidentTicket() throws Exception {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("INCIDENT");
        ticket.setSystem("Billing");
        ticket.setCreatedDate(LocalDate.now());
        ticket.setPriority("HIGH");
        ticket.setDescription("UI not loading");
        ticket.setResponsible("John Doe");
        ticket.setCategory("UI");
        ticket.setImpact("Users cannot access system");

        mockMvc.perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("VALID"))
                .andExpect(jsonPath("$.message").value("Ticket is valid"));
    }

    // ❌ Test 2: Invalid input (missing required fields + invalid priority)
    @Test
    void shouldFailValidationForInvalidTicket() throws Exception {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("INCIDENT");
        ticket.setSystem(""); // Blank
        ticket.setCreatedDate(LocalDate.now().plusDays(1)); // Future date
        ticket.setPriority("INVALID_PRIORITY"); // Not in allowed list
        ticket.setDescription(""); // Blank
        ticket.setResponsible(""); // Blank
        // Missing category and impact (type-specific fields)

        mockMvc.perform(post("/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failed"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    // 📦 Test 3: Bulk ticket validation (1 valid + 1 invalid)
    @Test
    void shouldHandleBulkValidation() throws Exception {
        TicketDTO validTicket = new TicketDTO();
        validTicket.setType("INCIDENT");
        validTicket.setSystem("CRM");
        validTicket.setCreatedDate(LocalDate.now());
        validTicket.setPriority("LOW");
        validTicket.setDescription("Access issue");
        validTicket.setResponsible("Jane Smith");
        validTicket.setCategory("Access Issue");
        validTicket.setImpact("Customer support can't login");

        TicketDTO invalidTicket = new TicketDTO();
        invalidTicket.setType("CHANGE_REQUEST");
        invalidTicket.setSystem(""); // Invalid
        invalidTicket.setCreatedDate(LocalDate.now());
        invalidTicket.setPriority("HIGH");
        invalidTicket.setDescription("Deploy patch");
        invalidTicket.setResponsible("Mark Twain");
        // Missing planned execution date and approver

        List<TicketDTO> bulkTickets = List.of(validTicket, invalidTicket);

        mockMvc.perform(post("/validate/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bulkTickets)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("success"))
                .andExpect(jsonPath("$[1].status").value("failed"))
                .andExpect(jsonPath("$[1].errors").isArray());
    }
}
