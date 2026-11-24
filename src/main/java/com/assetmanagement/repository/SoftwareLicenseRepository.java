package com.assetmanagement.repository;

import com.assetmanagement.model.LicenseStatus;
import com.assetmanagement.model.SoftwareLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SoftwareLicenseRepository extends JpaRepository<SoftwareLicense, Long> {
    List<SoftwareLicense> findByTeamId(Long teamId);
    List<SoftwareLicense> findByStatus(LicenseStatus status);
    List<SoftwareLicense> findByExpiryDateBetween(LocalDate start, LocalDate end);
    List<SoftwareLicense> findByExpiryDateBeforeAndStatusNot(LocalDate date, LicenseStatus status);
    List<SoftwareLicense> findBySoftwareNameContainingIgnoreCase(String name);
}
