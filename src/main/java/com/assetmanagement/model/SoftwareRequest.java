package com.assetmanagement.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "software_requests")
public class SoftwareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(nullable = false)
    private String softwareName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseType licenseType;

    @Column(nullable = false, length = 1000)
    private String justification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING_MANAGER_APPROVAL;

    private String managerApprovalNotes;
    private String adminApprovalNotes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_license_id")
    private SoftwareLicense assignedLicense;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    public SoftwareRequest() {}

    public SoftwareRequest(User requester, String softwareName, LicenseType licenseType, String justification) {
        this.requester = requester;
        this.softwareName = softwareName;
        this.licenseType = licenseType;
        this.justification = justification;
        this.status = RequestStatus.PENDING_MANAGER_APPROVAL;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getRequester() { return requester; }
    public void setRequester(User requester) { this.requester = requester; }

    public String getSoftwareName() { return softwareName; }
    public void setSoftwareName(String softwareName) { this.softwareName = softwareName; }

    public LicenseType getLicenseType() { return licenseType; }
    public void setLicenseType(LicenseType licenseType) { this.licenseType = licenseType; }

    public String getJustification() { return justification; }
    public void setJustification(String justification) { this.justification = justification; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getManagerApprovalNotes() { return managerApprovalNotes; }
    public void setManagerApprovalNotes(String managerApprovalNotes) { this.managerApprovalNotes = managerApprovalNotes; }

    public String getAdminApprovalNotes() { return adminApprovalNotes; }
    public void setAdminApprovalNotes(String adminApprovalNotes) { this.adminApprovalNotes = adminApprovalNotes; }

    public SoftwareLicense getAssignedLicense() { return assignedLicense; }
    public void setAssignedLicense(SoftwareLicense assignedLicense) { this.assignedLicense = assignedLicense; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SoftwareRequest that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
