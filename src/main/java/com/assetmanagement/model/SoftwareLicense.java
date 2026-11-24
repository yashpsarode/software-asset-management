package com.assetmanagement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "software_licenses")
public class SoftwareLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String softwareName;

    @Column(nullable = false)
    private String vendor;

    @Column(nullable = false, unique = true)
    private String licenseKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseType licenseType;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer allocatedSeats = 0;

    @Column(nullable = false)
    private BigDecimal cost;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseStatus status = LicenseStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    private Team team;

    public SoftwareLicense() {}

    public SoftwareLicense(String softwareName, String vendor, String licenseKey, LicenseType licenseType,
                           Integer totalSeats, BigDecimal cost, LocalDate purchaseDate, LocalDate expiryDate, Team team) {
        this.softwareName = softwareName;
        this.vendor = vendor;
        this.licenseKey = licenseKey;
        this.licenseType = licenseType;
        this.totalSeats = totalSeats;
        this.allocatedSeats = 0;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.team = team;
        this.status = LicenseStatus.ACTIVE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Integer getAllocatedSeats() { return allocatedSeats; }
    public void setAllocatedSeats(Integer allocatedSeats) { this.allocatedSeats = allocatedSeats; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public LicenseStatus getStatus() { return status; }
    public void setStatus(LicenseStatus status) { this.status = status; }

    public Team getTeam() { return team; }
    public void setTeam(Team team) { this.team = team; }

    public int getAvailableSeats() {
        return Math.max(0, (totalSeats != null ? totalSeats : 0) - (allocatedSeats != null ? allocatedSeats : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SoftwareLicense that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
