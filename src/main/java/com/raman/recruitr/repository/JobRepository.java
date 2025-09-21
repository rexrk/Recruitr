package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByOrganizationId(Long organizationId);
    Optional<Job> findByIdAndOrganizationId(Long jobId, Long organizationId);
    Optional<Job> findByIdAndOrganizationIdAndStatus(Long jobId, Long orgId, Job.JobStatus status);

    long countByOrganizationId(Long orgId);

    @Query("""
        SELECT COUNT(DISTINCT j.id)
        FROM Job j
        JOIN JobAssignment ja ON ja.job.id = j.id
        WHERE j.organization.id = :orgId
          AND ja.status = 'SELECTED'
    """)
    long countJobsWithSelectedAssignments(@Param("orgId") Long orgId);
}
