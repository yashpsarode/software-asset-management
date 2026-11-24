package com.assetmanagement.dto;

import jakarta.validation.constraints.NotNull;

public class ApprovalDecisionDTO {
    @NotNull(message = "Approval status (true/false) is required")
    private Boolean approved;

    private String notes;

    private Long assignedLicenseId; // Optional: specify license to assign upon admin approval

    public ApprovalDecisionDTO() {}

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Long getAssignedLicenseId() { return assignedLicenseId; }
    public void setAssignedLicenseId(Long assignedLicenseId) { this.assignedLicenseId = assignedLicenseId; }
}
