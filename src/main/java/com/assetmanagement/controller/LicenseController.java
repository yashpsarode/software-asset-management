package com.assetmanagement.controller;

import com.assetmanagement.dto.ApiResponse;
import com.assetmanagement.dto.LicenseSummaryDTO;
import com.assetmanagement.dto.SoftwareLicenseDTO;
import com.assetmanagement.model.LicenseStatus;
import com.assetmanagement.model.SoftwareLicense;
import com.assetmanagement.service.LicenseMonitorService;
import com.assetmanagement.service.LicenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {

    private final LicenseService licenseService;
    private final LicenseMonitorService monitorService;

    public LicenseController(LicenseService licenseService, LicenseMonitorService monitorService) {
        this.licenseService = licenseService;
        this.monitorService = monitorService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SoftwareLicense>>> getAllLicenses(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) LicenseStatus status,
            @RequestParam(required = false) String search) {
        List<SoftwareLicense> licenses = licenseService.getAllLicenses(teamId, status, search);
        return ResponseEntity.ok(ApiResponse.ok("Licenses retrieved successfully", licenses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SoftwareLicense>> getLicenseById(@PathVariable Long id) {
        SoftwareLicense license = licenseService.getLicenseById(id);
        return ResponseEntity.ok(ApiResponse.ok("License retrieved", license));
    }

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<LicenseSummaryDTO>> getSummary() {
        LicenseSummaryDTO summary = licenseService.getSummary();
        return ResponseEntity.ok(ApiResponse.ok("License summary retrieved", summary));
    }

    @GetMapping("/expiring")
    public ResponseEntity<ApiResponse<List<SoftwareLicense>>> getExpiringLicenses(
            @RequestParam(defaultValue = "30") int days) {
        List<SoftwareLicense> expiring = licenseService.getExpiringLicenses(days);
        return ResponseEntity.ok(ApiResponse.ok("Licenses expiring within " + days + " days retrieved", expiring));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SoftwareLicense>> createLicense(@Valid @RequestBody SoftwareLicenseDTO dto) {
        SoftwareLicense created = licenseService.createLicense(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Software license created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SoftwareLicense>> updateLicense(
            @PathVariable Long id, @Valid @RequestBody SoftwareLicenseDTO dto) {
        SoftwareLicense updated = licenseService.updateLicense(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Software license updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLicense(@PathVariable Long id) {
        licenseService.deleteLicense(id);
        return ResponseEntity.ok(ApiResponse.ok("License deleted successfully", null));
    }

    @PostMapping("/monitor-audit")
    public ResponseEntity<ApiResponse<String>> runManualExpiryAudit() {
        int updated = monitorService.monitorAndRefreshStatuses();
        return ResponseEntity.ok(ApiResponse.ok("Manual expiry audit executed", "Updated " + updated + " license statuses"));
    }
}
