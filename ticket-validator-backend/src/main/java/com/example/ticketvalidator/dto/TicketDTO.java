package com.example.ticketvalidator.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TicketDTO {

    @NotNull(message = "Type is required")
    private String type;

    @NotBlank(message = "System must not be empty")
    private String system;

    @NotNull(message = "Created date is required")
    @PastOrPresent(message = "Created date cannot be in the future")
    private LocalDate createdDate;

    @NotNull(message = "Priority is required")
    @Pattern(
            regexp = "LOW|MEDIUM|HIGH|CRITICAL",
            message = "Priority must be one of LOW, MEDIUM, HIGH, CRITICAL (case-insensitive)"
    )
    private String priority;

    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotBlank(message = "Responsible must not be empty")
    private String responsible;

    // ----------- Type-specific fields (optional at first) -----------

    // INCIDENT
    private String category;
    private String impact;

    // CHANGE_REQUEST
    private LocalDate plannedExecutionDate;
    private String approver;

    // MAINTENANCE
    private LocalDateTime maintenanceWindowStart;
    private LocalDateTime maintenanceWindowEnd;
    private List<String> affectedComponents;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public LocalDate getPlannedExecutionDate() {
        return plannedExecutionDate;
    }

    public void setPlannedExecutionDate(LocalDate plannedExecutionDate) {
        this.plannedExecutionDate = plannedExecutionDate;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public LocalDateTime getMaintenanceWindowStart() {
        return maintenanceWindowStart;
    }

    public void setMaintenanceWindowStart(LocalDateTime maintenance_window_start) {
        this.maintenanceWindowStart = maintenance_window_start;
    }

    public LocalDateTime getMaintenanceWindowEnd() {
        return maintenanceWindowEnd;
    }

    public void setMaintenanceWindowEnd(LocalDateTime maintenance_window_end) {
        this.maintenanceWindowEnd = maintenance_window_end;
    }

    public List<String> getAffectedComponents() {
        return affectedComponents;
    }

    public void setAffectedComponents(List<String> affectedComponents) {
        this.affectedComponents = affectedComponents;
    }

}