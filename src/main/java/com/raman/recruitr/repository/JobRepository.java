package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByOrganizationId(Long organizationId);
    Optional<Job> findByIdAndOrganizationId(Long jobId, Long organizationId);
    Optional<Job> findByIdAndOrganizationIdAndStatus(Long jobId, Long orgId, Job.JobStatus status);

}
