package com.assetmanagement.service;

import com.assetmanagement.dto.LicenseSummaryDTO;
import com.assetmanagement.dto.SoftwareLicenseDTO;
import com.assetmanagement.model.LicenseStatus;
import com.assetmanagement.model.SoftwareLicense;
import com.assetmanagement.model.Team;
import com.assetmanagement.repository.SoftwareLicenseRepository;
import com.assetmanagement.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LicenseService {

    private final SoftwareLicenseRepository licenseRepository;
    private final TeamRepository teamRepository;

    public LicenseService(SoftwareLicenseRepository licenseRepository, TeamRepository teamRepository) {
        this.licenseRepository = licenseRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public SoftwareLicense createLicense(SoftwareLicenseDTO dto) {
        Team team = null;
        if (dto.getTeamId() != null) {
            team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + dto.getTeamId()));
        }

        SoftwareLicense license = new SoftwareLicense(
                dto.getSoftwareName(),
                dto.getVendor(),
                dto.getLicenseKey(),
                dto.getLicenseType(),
                dto.getTotalSeats(),
                dto.getCost(),
                dto.getPurchaseDate(),
                dto.getExpiryDate(),
                team
        );
        license.setStatus(calculateStatus(dto.getExpiryDate()));
        return licenseRepository.save(license);
    }

    @Transactional(readOnly = true)
    public List<SoftwareLicense> getAllLicenses(Long teamId, LicenseStatus status, String search) {
        if (search != null && !search.isBlank()) {
            return licenseRepository.findBySoftwareNameContainingIgnoreCase(search.trim());
        }
        if (teamId != null) {
            return licenseRepository.findByTeamId(teamId);
        }
        if (status != null) {
            return licenseRepository.findByStatus(status);
        }
        return licenseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SoftwareLicense getLicenseById(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("License not found with ID: " + id));
    }

    @Transactional
    public SoftwareLicense updateLicense(Long id, SoftwareLicenseDTO dto) {
        SoftwareLicense license = getLicenseById(id);
        license.setSoftwareName(dto.getSoftwareName());
        license.setVendor(dto.getVendor());
        license.setLicenseKey(dto.getLicenseKey());
        license.setLicenseType(dto.getLicenseType());
        license.setTotalSeats(dto.getTotalSeats());
        license.setCost(dto.getCost());
        license.setPurchaseDate(dto.getPurchaseDate());
        license.setExpiryDate(dto.getExpiryDate());

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + dto.getTeamId()));
            license.setTeam(team);
        } else {
            license.setTeam(null);
        }

        license.setStatus(calculateStatus(license.getExpiryDate()));
        return licenseRepository.save(license);
    }

    @Transactional
    public void deleteLicense(Long id) {
        if (!licenseRepository.existsById(id)) {
            throw new IllegalArgumentException("License not found with ID: " + id);
        }
        licenseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SoftwareLicense> getExpiringLicenses(int withinDays) {
        LocalDate today = LocalDate.now();
        LocalDate threshold = today.plusDays(withinDays);
        return licenseRepository.findByExpiryDateBetween(today, threshold);
    }

    @Transactional(readOnly = true)
    public LicenseSummaryDTO getSummary() {
        List<SoftwareLicense> all = licenseRepository.findAll();
        long total = all.size();
        long active = all.stream().filter(l -> l.getStatus() == LicenseStatus.ACTIVE).count();
        long expiringSoon = all.stream().filter(l -> l.getStatus() == LicenseStatus.EXPIRING_SOON).count();
        long expired = all.stream().filter(l -> l.getStatus() == LicenseStatus.EXPIRED).count();

        int totalSeats = all.stream().mapToInt(l -> l.getTotalSeats() != null ? l.getTotalSeats() : 0).sum();
        int allocatedSeats = all.stream().mapToInt(l -> l.getAllocatedSeats() != null ? l.getAllocatedSeats() : 0).sum();
        BigDecimal totalSpend = all.stream()
                .map(l -> l.getCost() != null ? l.getCost() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LicenseSummaryDTO(total, active, expiringSoon, expired, totalSeats, allocatedSeats, totalSpend);
    }

    public LicenseStatus calculateStatus(LocalDate expiryDate) {
        if (expiryDate == null) return LicenseStatus.ACTIVE;
        LocalDate today = LocalDate.now();
        if (expiryDate.isBefore(today)) {
            return LicenseStatus.EXPIRED;
        } else if (expiryDate.isBefore(today.plusDays(30))) {
            return LicenseStatus.EXPIRING_SOON;
        }
        return LicenseStatus.ACTIVE;
    }
}
