package com.assetmanagement.service;

import com.assetmanagement.dto.RenewalRequestDTO;
import com.assetmanagement.model.LicenseStatus;
import com.assetmanagement.model.RenewalHistory;
import com.assetmanagement.model.SoftwareLicense;
import com.assetmanagement.model.User;
import com.assetmanagement.repository.RenewalHistoryRepository;
import com.assetmanagement.repository.SoftwareLicenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RenewalService {

    private final SoftwareLicenseRepository licenseRepository;
    private final RenewalHistoryRepository renewalHistoryRepository;

    public RenewalService(SoftwareLicenseRepository licenseRepository, RenewalHistoryRepository renewalHistoryRepository) {
        this.licenseRepository = licenseRepository;
        this.renewalHistoryRepository = renewalHistoryRepository;
    }

    @Transactional
    public RenewalHistory renewLicense(Long licenseId, RenewalRequestDTO dto, User renewedBy) {
        SoftwareLicense license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new IllegalArgumentException("License not found with ID: " + licenseId));

        if (dto.getNewExpiryDate().isBefore(license.getExpiryDate())) {
            throw new IllegalArgumentException("New expiry date must be later than current expiry date (" + license.getExpiryDate() + ")");
        }

        LocalDate previousExpiryDate = license.getExpiryDate();
        license.setExpiryDate(dto.getNewExpiryDate());
        license.setCost(license.getCost().add(dto.getRenewalCost()));

        // If renewed, reset status to ACTIVE unless new date is already within 30 days
        LocalDate today = LocalDate.now();
        if (dto.getNewExpiryDate().isAfter(today.plusDays(30))) {
            license.setStatus(LicenseStatus.ACTIVE);
        } else if (dto.getNewExpiryDate().isAfter(today)) {
            license.setStatus(LicenseStatus.EXPIRING_SOON);
        } else {
            license.setStatus(LicenseStatus.EXPIRED);
        }

        licenseRepository.save(license);

        RenewalHistory history = new RenewalHistory(
                license,
                previousExpiryDate,
                dto.getNewExpiryDate(),
                dto.getRenewalCost(),
                renewedBy,
                dto.getRemarks()
        );
        history.setRenewalTimestamp(LocalDateTime.now());

        return renewalHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<RenewalHistory> getRenewalHistoryByLicense(Long licenseId) {
        return renewalHistoryRepository.findBySoftwareLicenseIdOrderByRenewalTimestampDesc(licenseId);
    }

    @Transactional(readOnly = true)
    public List<RenewalHistory> getAllRenewalHistories() {
        return renewalHistoryRepository.findAllByOrderByRenewalTimestampDesc();
    }
}
