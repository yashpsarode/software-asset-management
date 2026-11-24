package com.assetmanagement.controller;

import com.assetmanagement.dto.ApiResponse;
import com.assetmanagement.dto.RenewalRequestDTO;
import com.assetmanagement.model.RenewalHistory;
import com.assetmanagement.model.User;
import com.assetmanagement.service.CustomUserDetailsService;
import com.assetmanagement.service.RenewalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renewals")
public class RenewalController {

    private final RenewalService renewalService;
    private final CustomUserDetailsService userDetailsService;

    public RenewalController(RenewalService renewalService, CustomUserDetailsService userDetailsService) {
        this.renewalService = renewalService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/renew/{licenseId}")
    public ResponseEntity<ApiResponse<RenewalHistory>> renewLicense(
            @PathVariable Long licenseId,
            @Valid @RequestBody RenewalRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User renewedBy = userDetailsService.getFullUser(userDetails.getUsername());
        RenewalHistory history = renewalService.renewLicense(licenseId, dto, renewedBy);
        return ResponseEntity.ok(ApiResponse.ok("Software license renewed successfully", history));
    }

    @GetMapping("/license/{licenseId}")
    public ResponseEntity<ApiResponse<List<RenewalHistory>>> getRenewalHistoryByLicense(@PathVariable Long licenseId) {
        List<RenewalHistory> histories = renewalService.getRenewalHistoryByLicense(licenseId);
        return ResponseEntity.ok(ApiResponse.ok("Renewal history for license retrieved", histories));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RenewalHistory>>> getAllRenewalHistories() {
        List<RenewalHistory> histories = renewalService.getAllRenewalHistories();
        return ResponseEntity.ok(ApiResponse.ok("All renewal histories retrieved", histories));
    }
}
