package com.assetmanagement.repository;

import com.assetmanagement.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByDepartmentCode(String departmentCode);
    Optional<Team> findByName(String name);
}
