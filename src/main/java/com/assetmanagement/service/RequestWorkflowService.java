package com.assetmanagement.service;

import com.assetmanagement.dto.ApprovalDecisionDTO;
import com.assetmanagement.dto.SoftwareRequestCreateDTO;
import com.assetmanagement.model.*;
import com.assetmanagement.repository.SoftwareLicenseRepository;
import com.assetmanagement.repository.SoftwareRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestWorkflowService {

    private final SoftwareRequestRepository requestRepository;
    private final SoftwareLicenseRepository licenseRepository;

    public RequestWorkflowService(SoftwareRequestRepository requestRepository, SoftwareLicenseRepository licenseRepository) {
        this.requestRepository = requestRepository;
        this.licenseRepository = licenseRepository;
    }

    @Transactional
    public SoftwareRequest createRequest(SoftwareRequestCreateDTO dto, User requester) {
        SoftwareRequest request = new SoftwareRequest(
                requester,
                dto.getSoftwareName(),
                dto.getLicenseType(),
                dto.getJustification()
        );
        return requestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public List<SoftwareRequest> getRequestsForUser(User user) {
        if (user.getRole() == Role.ROLE_ADMIN) {
            return requestRepository.findAll();
        } else if (user.getRole() == Role.ROLE_MANAGER) {
            if (user.getTeam() != null) {
                return requestRepository.findByRequesterTeamId(user.getTeam().getId());
            }
        }
        return requestRepository.findByRequesterId(user.getId());
    }

    @Transactional(readOnly = true)
    public List<SoftwareRequest> getPendingManagerApprovals(User manager) {
        if (manager.getTeam() == null) {
            return List.of();
        }
        return requestRepository.findByRequesterTeamIdAndStatus(manager.getTeam().getId(), RequestStatus.PENDING_MANAGER_APPROVAL);
    }

    @Transactional(readOnly = true)
    public List<SoftwareRequest> getPendingAdminApprovals() {
        return requestRepository.findByStatus(RequestStatus.PENDING_ADMIN_APPROVAL);
    }

    @Transactional
    public SoftwareRequest processManagerApproval(Long requestId, ApprovalDecisionDTO decision, User manager) {
        SoftwareRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with ID: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING_MANAGER_APPROVAL) {
            throw new IllegalStateException("Request is not awaiting Manager approval (Current status: " + request.getStatus() + ")");
        }

        // Verify manager team scope
        if (manager.getRole() != Role.ROLE_ADMIN) {
            if (request.getRequester().getTeam() == null ||
                    manager.getTeam() == null ||
                    !request.getRequester().getTeam().getId().equals(manager.getTeam().getId())) {
                throw new SecurityException("Managers can only approve requests for their assigned team members");
            }
        }

        request.setManagerApprovalNotes(decision.getNotes());

        if (Boolean.TRUE.equals(decision.getApproved())) {
            // Passes level-1 approval, advances to Admin level-2 approval
            request.setStatus(RequestStatus.PENDING_ADMIN_APPROVAL);
        } else {
            // Rejected at level 1
            request.setStatus(RequestStatus.REJECTED);
            request.setResolvedAt(LocalDateTime.now());
        }

        return requestRepository.save(request);
    }

    @Transactional
    public SoftwareRequest processAdminApproval(Long requestId, ApprovalDecisionDTO decision) {
        SoftwareRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found with ID: " + requestId));

        if (request.getStatus() != RequestStatus.PENDING_ADMIN_APPROVAL) {
            throw new IllegalStateException("Request is not awaiting Admin approval (Current status: " + request.getStatus() + ")");
        }

        request.setAdminApprovalNotes(decision.getNotes());

        if (Boolean.TRUE.equals(decision.getApproved())) {
            request.setStatus(RequestStatus.APPROVED);
            request.setResolvedAt(LocalDateTime.now());

            // Optional: allocate license seat if assignedLicenseId was provided
            if (decision.getAssignedLicenseId() != null) {
                SoftwareLicense license = licenseRepository.findById(decision.getAssignedLicenseId())
                        .orElseThrow(() -> new IllegalArgumentException("Assigned license not found"));

                if (license.getAvailableSeats() <= 0) {
                    throw new IllegalStateException("License " + license.getSoftwareName() + " has no available seats remaining");
                }
                license.setAllocatedSeats(license.getAllocatedSeats() + 1);
                licenseRepository.save(license);
                request.setAssignedLicense(license);
            }
        } else {
            request.setStatus(RequestStatus.REJECTED);
            request.setResolvedAt(LocalDateTime.now());
        }

        return requestRepository.save(request);
    }
}
