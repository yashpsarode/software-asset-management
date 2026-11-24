package com.assetmanagement.controller;

import com.assetmanagement.dto.ApiResponse;
import com.assetmanagement.dto.ApprovalDecisionDTO;
import com.assetmanagement.dto.SoftwareRequestCreateDTO;
import com.assetmanagement.model.SoftwareRequest;
import com.assetmanagement.model.User;
import com.assetmanagement.service.CustomUserDetailsService;
import com.assetmanagement.service.RequestWorkflowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class SoftwareRequestController {

    private final RequestWorkflowService workflowService;
    private final CustomUserDetailsService userDetailsService;

    public SoftwareRequestController(RequestWorkflowService workflowService, CustomUserDetailsService userDetailsService) {
        this.workflowService = workflowService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SoftwareRequest>> createRequest(
            @Valid @RequestBody SoftwareRequestCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User requester = userDetailsService.getFullUser(userDetails.getUsername());
        SoftwareRequest request = workflowService.createRequest(dto, requester);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Software request submitted successfully", request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SoftwareRequest>>> getRequests(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userDetailsService.getFullUser(userDetails.getUsername());
        List<SoftwareRequest> requests = workflowService.getRequestsForUser(user);
        return ResponseEntity.ok(ApiResponse.ok("Requests retrieved successfully", requests));
    }

    @GetMapping("/pending-manager")
    public ResponseEntity<ApiResponse<List<SoftwareRequest>>> getPendingManagerApprovals(
            @AuthenticationPrincipal UserDetails userDetails) {
        User manager = userDetailsService.getFullUser(userDetails.getUsername());
        List<SoftwareRequest> pending = workflowService.getPendingManagerApprovals(manager);
        return ResponseEntity.ok(ApiResponse.ok("Pending manager approvals retrieved", pending));
    }

    @GetMapping("/pending-admin")
    public ResponseEntity<ApiResponse<List<SoftwareRequest>>> getPendingAdminApprovals() {
        List<SoftwareRequest> pending = workflowService.getPendingAdminApprovals();
        return ResponseEntity.ok(ApiResponse.ok("Pending admin approvals retrieved", pending));
    }

    @PostMapping("/manager-review/{requestId}")
    public ResponseEntity<ApiResponse<SoftwareRequest>> managerReview(
            @PathVariable Long requestId,
            @Valid @RequestBody ApprovalDecisionDTO decision,
            @AuthenticationPrincipal UserDetails userDetails) {
        User manager = userDetailsService.getFullUser(userDetails.getUsername());
        SoftwareRequest updated = workflowService.processManagerApproval(requestId, decision, manager);
        return ResponseEntity.ok(ApiResponse.ok("Manager review recorded successfully", updated));
    }

    @PostMapping("/admin-review/{requestId}")
    public ResponseEntity<ApiResponse<SoftwareRequest>> adminReview(
            @PathVariable Long requestId,
            @Valid @RequestBody ApprovalDecisionDTO decision) {
        SoftwareRequest updated = workflowService.processAdminApproval(requestId, decision);
        return ResponseEntity.ok(ApiResponse.ok("Admin review recorded successfully", updated));
    }
}
