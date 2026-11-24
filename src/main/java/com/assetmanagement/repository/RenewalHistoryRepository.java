package com.assetmanagement.repository;

import com.assetmanagement.model.RenewalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RenewalHistoryRepository extends JpaRepository<RenewalHistory, Long> {
    List<RenewalHistory> findBySoftwareLicenseIdOrderByRenewalTimestampDesc(Long licenseId);
    List<RenewalHistory> findAllByOrderByRenewalTimestampDesc();
}
