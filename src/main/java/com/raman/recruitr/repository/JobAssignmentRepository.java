package com.raman.recruitr.repository;

import com.raman.recruitr.entity.JobAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobAssignmentRepository extends JpaRepository<JobAssignment, Long> {
    List<JobAssignment> findAllByJobId(Long jobId);
    Optional<JobAssignment> findByIdAndJobOrganizationId(Long id, Long orgId);
}

