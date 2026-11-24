package com.assetmanagement.service;

import com.assetmanagement.model.LicenseStatus;
import com.assetmanagement.model.SoftwareLicense;
import com.assetmanagement.repository.SoftwareLicenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LicenseMonitorService {

    private static final Logger log = LoggerFactory.getLogger(LicenseMonitorService.class);

    private final SoftwareLicenseRepository licenseRepository;

    public LicenseMonitorService(SoftwareLicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    // Run automatically every day at 1:00 AM
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional
    public void runDailyExpiryAudit() {
        log.info("Starting automated software license expiry monitoring audit...");
        int updatedCount = monitorAndRefreshStatuses();
        log.info("Completed license expiry audit. Updated {} license statuses.", updatedCount);
    }

    @Transactional
    public int monitorAndRefreshStatuses() {
        List<SoftwareLicense> allLicenses = licenseRepository.findAll();
        LocalDate today = LocalDate.now();
        LocalDate warningThreshold = today.plusDays(30);

        int updatedCount = 0;
        for (SoftwareLicense license : allLicenses) {
            LicenseStatus newStatus;
            if (license.getExpiryDate().isBefore(today)) {
                newStatus = LicenseStatus.EXPIRED;
            } else if (license.getExpiryDate().isBefore(warningThreshold)) {
                newStatus = LicenseStatus.EXPIRING_SOON;
            } else {
                newStatus = LicenseStatus.ACTIVE;
            }

            if (license.getStatus() != newStatus) {
                license.setStatus(newStatus);
                licenseRepository.save(license);
                updatedCount++;
            }
        }
        return updatedCount;
    }
}
