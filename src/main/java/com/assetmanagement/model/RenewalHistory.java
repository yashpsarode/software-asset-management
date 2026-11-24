package com.assetmanagement.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "renewal_histories")
public class RenewalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "license_id", nullable = false)
    private SoftwareLicense softwareLicense;

    @Column(nullable = false)
    private LocalDate previousExpiryDate;

    @Column(nullable = false)
    private LocalDate newExpiryDate;

    @Column(nullable = false)
    private BigDecimal renewalCost;

    @Column(nullable = false)
    private LocalDateTime renewalTimestamp = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "renewed_by_id", nullable = false)
    private User renewedBy;

    private String remarks;

    public RenewalHistory() {}

    public RenewalHistory(SoftwareLicense softwareLicense, LocalDate previousExpiryDate, LocalDate newExpiryDate,
                          BigDecimal renewalCost, User renewedBy, String remarks) {
        this.softwareLicense = softwareLicense;
        this.previousExpiryDate = previousExpiryDate;
        this.newExpiryDate = newExpiryDate;
        this.renewalCost = renewalCost;
        this.renewedBy = renewedBy;
        this.remarks = remarks;
        this.renewalTimestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public SoftwareLicense getSoftwareLicense() { return softwareLicense; }
    public void setSoftwareLicense(SoftwareLicense softwareLicense) { this.softwareLicense = softwareLicense; }

    public LocalDate getPreviousExpiryDate() { return previousExpiryDate; }
    public void setPreviousExpiryDate(LocalDate previousExpiryDate) { this.previousExpiryDate = previousExpiryDate; }

    public LocalDate getNewExpiryDate() { return newExpiryDate; }
    public void setNewExpiryDate(LocalDate newExpiryDate) { this.newExpiryDate = newExpiryDate; }

    public BigDecimal getRenewalCost() { return renewalCost; }
    public void setRenewalCost(BigDecimal renewalCost) { this.renewalCost = renewalCost; }

    public LocalDateTime getRenewalTimestamp() { return renewalTimestamp; }
    public void setRenewalTimestamp(LocalDateTime renewalTimestamp) { this.renewalTimestamp = renewalTimestamp; }

    public User getRenewedBy() { return renewedBy; }
    public void setRenewedBy(User renewedBy) { this.renewedBy = renewedBy; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RenewalHistory that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
