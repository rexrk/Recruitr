package com.raman.recruitr.service;

import com.raman.recruitr.repository.JobAssignmentRepository;
import com.raman.recruitr.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final JobRepository jobRepository;
    private final JobAssignmentRepository jobAssignmentRepository;

    public double getJobFillRate(Long orgId) {
        long totalJobs = jobRepository.countByOrganizationId(orgId);
        if (totalJobs == 0) return 0.0;

        long filledJobs = jobRepository.countJobsWithSelectedAssignments(orgId);

        return (filledJobs * 100.0) / totalJobs;
    }
}
