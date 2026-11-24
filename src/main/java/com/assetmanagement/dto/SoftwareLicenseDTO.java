package com.assetmanagement.dto;

import com.assetmanagement.model.LicenseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SoftwareLicenseDTO {
    @NotBlank(message = "Software name is required")
    private String softwareName;

    @NotBlank(message = "Vendor is required")
    private String vendor;

    @NotBlank(message = "License key is required")
    private String licenseKey;

    @NotNull(message = "License type is required")
    private LicenseType licenseType;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be greater than zero")
    private Integer totalSeats;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be positive")
    private BigDecimal cost;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;

    private Long teamId;

    public SoftwareLicenseDTO() {}

    public String getSoftwareName() { return softwareName; }
    public void setSoftwareName(String softwareName) { this.softwareName = softwareName; }

    public String getVendor() { return vendor; }
    public void setVendor(String vendor) { this.vendor = vendor; }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }

    public LicenseType getLicenseType() { return licenseType; }
    public void setLicenseType(LicenseType licenseType) { this.licenseType = licenseType; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
}
