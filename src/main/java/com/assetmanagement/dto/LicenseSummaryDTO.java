package com.assetmanagement.dto;

import java.math.BigDecimal;

public class LicenseSummaryDTO {
    private long totalLicenses;
    private long activeLicenses;
    private long expiringSoonLicenses;
    private long expiredLicenses;
    private int totalSeats;
    private int allocatedSeats;
    private int availableSeats;
    private BigDecimal totalSpend;

    public LicenseSummaryDTO() {}

    public LicenseSummaryDTO(long totalLicenses, long activeLicenses, long expiringSoonLicenses,
                             long expiredLicenses, int totalSeats, int allocatedSeats, BigDecimal totalSpend) {
        this.totalLicenses = totalLicenses;
        this.activeLicenses = activeLicenses;
        this.expiringSoonLicenses = expiringSoonLicenses;
        this.expiredLicenses = expiredLicenses;
        this.totalSeats = totalSeats;
        this.allocatedSeats = allocatedSeats;
        this.availableSeats = Math.max(0, totalSeats - allocatedSeats);
        this.totalSpend = totalSpend;
    }

    public long getTotalLicenses() { return totalLicenses; }
    public void setTotalLicenses(long totalLicenses) { this.totalLicenses = totalLicenses; }

    public long getActiveLicenses() { return activeLicenses; }
    public void setActiveLicenses(long activeLicenses) { this.activeLicenses = activeLicenses; }

    public long getExpiringSoonLicenses() { return expiringSoonLicenses; }
    public void setExpiringSoonLicenses(long expiringSoonLicenses) { this.expiringSoonLicenses = expiringSoonLicenses; }

    public long getExpiredLicenses() { return expiredLicenses; }
    public void setExpiredLicenses(long expiredLicenses) { this.expiredLicenses = expiredLicenses; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getAllocatedSeats() { return allocatedSeats; }
    public void setAllocatedSeats(int allocatedSeats) { this.allocatedSeats = allocatedSeats; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public BigDecimal getTotalSpend() { return totalSpend; }
    public void setTotalSpend(BigDecimal totalSpend) { this.totalSpend = totalSpend; }
}
