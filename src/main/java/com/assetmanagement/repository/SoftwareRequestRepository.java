package com.assetmanagement.repository;

import com.assetmanagement.model.RequestStatus;
import com.assetmanagement.model.SoftwareRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoftwareRequestRepository extends JpaRepository<SoftwareRequest, Long> {
    List<SoftwareRequest> findByRequesterId(Long requesterId);
    List<SoftwareRequest> findByStatus(RequestStatus status);
    List<SoftwareRequest> findByRequesterTeamIdAndStatus(Long teamId, RequestStatus status);
    List<SoftwareRequest> findByRequesterTeamId(Long teamId);
}
