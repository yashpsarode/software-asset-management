package com.assetmanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RenewalRequestDTO {
    @NotNull(message = "New expiry date is required")
    private LocalDate newExpiryDate;

    @NotNull(message = "Renewal cost is required")
    @Positive(message = "Renewal cost must be positive")
    private BigDecimal renewalCost;

    private String remarks;

    public RenewalRequestDTO() {}

    public LocalDate getNewExpiryDate() { return newExpiryDate; }
    public void setNewExpiryDate(LocalDate newExpiryDate) { this.newExpiryDate = newExpiryDate; }

    public BigDecimal getRenewalCost() { return renewalCost; }
    public void setRenewalCost(BigDecimal renewalCost) { this.renewalCost = renewalCost; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
