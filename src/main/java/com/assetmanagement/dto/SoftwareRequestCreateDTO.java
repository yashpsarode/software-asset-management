package com.assetmanagement.dto;

import com.assetmanagement.model.LicenseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SoftwareRequestCreateDTO {
    @NotBlank(message = "Software name is required")
    private String softwareName;

    @NotNull(message = "License type is required")
    private LicenseType licenseType;

    @NotBlank(message = "Justification is required")
    private String justification;

    public SoftwareRequestCreateDTO() {}

    public String getSoftwareName() { return softwareName; }
    public void setSoftwareName(String softwareName) { this.softwareName = softwareName; }

    public LicenseType getLicenseType() { return licenseType; }
    public void setLicenseType(LicenseType licenseType) { this.licenseType = licenseType; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }
}
