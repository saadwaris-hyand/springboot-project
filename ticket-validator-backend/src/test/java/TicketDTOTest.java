import com.example.ticketvalidator.TicketValidatorApplication;
import com.example.ticketvalidator.dto.TicketDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TicketValidatorApplication.class)
public class TicketDTOTest {

    private Validator validator;

    @BeforeEach
    void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private TicketDTO createValidTicket() {
        TicketDTO ticket = new TicketDTO();
        ticket.setType("INCIDENT");
        ticket.setSystem("System A");
        ticket.setCreatedDate(LocalDate.now());
        ticket.setPriority("HIGH");
        ticket.setDescription("Something broke");
        ticket.setResponsible("John Doe");
        return ticket;
    }

    @Test
    void testValidTicket_ShouldHaveNoViolations() {
        TicketDTO ticket = createValidTicket();
        Set<ConstraintViolation<TicketDTO>> violations = validator.validate(ticket);
        assertTrue(violations.isEmpty(), "There should be no validation errors");
    }

    @Test
    void testMissingRequiredFields_ShouldFailValidation() {
        TicketDTO ticket = new TicketDTO(); // Empty object
        Set<ConstraintViolation<TicketDTO>> violations = validator.validate(ticket);

        assertEquals(6, violations.size()); // All 6 required fields should fail

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("type")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("system")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("createdDate")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("priority")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("responsible")));
    }

    @Test
    void testCreatedDateInFuture_ShouldFailValidation() {
        TicketDTO ticket = createValidTicket();
        ticket.setCreatedDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<TicketDTO>> violations = validator.validate(ticket);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("createdDate") &&
                        v.getMessage().contains("Created date cannot be in the future")
        ));
    }

    @Test
    void testInvalidPriority_ShouldFailValidation() {
        TicketDTO ticket = createValidTicket();
        ticket.setPriority("URGENT"); // Not allowed

        Set<ConstraintViolation<TicketDTO>> violations = validator.validate(ticket);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v ->
                v.getPropertyPath().toString().equals("priority") &&
                        v.getMessage().contains("Priority must be one of LOW, MEDIUM, HIGH, CRITICAL")
        ));
    }

    @Test
    void testBlankFields_ShouldFailValidation() {
        TicketDTO ticket = createValidTicket();
        ticket.setSystem(" ");
        ticket.setResponsible(" ");
        ticket.setDescription(" ");

        Set<ConstraintViolation<TicketDTO>> violations = validator.validate(ticket);

        assertEquals(3, violations.size());
        assertTrue(violations.stream().allMatch(v ->
                v.getPropertyPath().toString().matches("system|responsible|description")
        ));
    }
}
