package com.raman.recruitr.service;

import com.raman.recruitr.entity.*;
import com.raman.recruitr.entity.dto.request.JobAssignmentRequest;
import com.raman.recruitr.entity.dto.request.JobRequest;
import com.raman.recruitr.entity.dto.response.*;
import com.raman.recruitr.exception.ResourceNotFoundException;
import com.raman.recruitr.repository.CandidateRepository;
import com.raman.recruitr.repository.JobAssignmentRepository;
import com.raman.recruitr.repository.JobRepository;
import com.raman.recruitr.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {
    private final JobRepository jobRepository;
    private final JobAssignmentRepository jobAssignmentRepository;
    private final CandidateRepository candidateRepository;

    // Helper Functions ======================================================================================

    private static Organization getOrganizationFromAuthenticatedUser() {
        AppUser orgAdmin = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (orgAdmin.getOrganization() == null) throw new ResourceNotFoundException("No Organization is assigned to your account");
        return orgAdmin.getOrganization();
    }


    // Service Functions ======================================================================================
    /**
     * Create a new job for the logged-in OrgAdmin's organization
     */
    public JobResponse createJob(final JobRequest request) {
        Organization org = getOrganizationFromAuthenticatedUser();

        Job job = new Job();
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setRequiredSkills(request.requiredSkills());
        job.setExperienceLevel(request.experienceLevel());
        job.setOrganization(org);

        Job saved = jobRepository.save(job);
        log.info("Job '{}' created for organization {}", saved.getTitle(), org.getCompanyName());

        return Utility.mapToResponse(saved);
    }

    /**
     * Fetch all jobs: OrgAdmin can only fetch jobs from their organization
     */
    public List<JobResponse> getAllJobs() {
        Organization org = getOrganizationFromAuthenticatedUser();
        List<Job> allJobs = jobRepository.findAllByOrganizationId(org.getId());
        log.info("Fetched {} jobs for organization {}", allJobs.size(), org.getCompanyName());

        return allJobs.stream().map(Utility::mapToResponse).toList();
    }

    /**
     * Fetch a job: OrgAdmin can only fetch job from their organization
     */
     public JobResponse getJobById(final Long jobId) {
        Organization org = getOrganizationFromAuthenticatedUser();
        Job job = jobRepository.findByIdAndOrganizationId(jobId, org.getId())
                 .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + jobId));

        log.info("Fetched job '{}' for organization {}", job.getTitle(), org.getCompanyName());

        return Utility.mapToResponse(job);
    }

    /**
     * Update a job: OrgAdmin can only close jobs from their organization
     */
    @Transactional
    public void closeJob(final Long jobId) {
        Organization org = getOrganizationFromAuthenticatedUser();

        Job job = jobRepository.findByIdAndOrganizationId(jobId, org.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + jobId));

        job.setStatus(Job.JobStatus.CLOSED);
        log.info("Job '{}' closed for organization {}", job.getTitle(), org.getCompanyName());

    }

    /**
     * Assign multiple candidates to a job
     */
    @Transactional
    public List<JobAssignmentResponse> assignCandidatesToJob(JobAssignmentRequest request) {
        Organization org = getOrganizationFromAuthenticatedUser();

        // Fetch the job by id, organization, and ensure it is OPEN
        Job job = jobRepository.findByIdAndOrganizationIdAndStatus(
                        request.jobId(),
                        org.getId(),
                        Job.JobStatus.OPEN
                )
                .orElseThrow(() -> new ResourceNotFoundException("Open job not found with id " + request.jobId()));
        log.info("Job {} is Open", job.getTitle());

        // Get IDs of valid candidates (own org + vendors)
        List<Candidate> candidates = candidateRepository.findValidCandidatesForJobAssignment(
                request.candidateIds(),
                org.getId()
        );
        log.info("Valid no. of candidates for this job: {}", candidates.size());

        // Create JobAssignment entities
        List<JobAssignment> assignments = candidates.stream().map(candidate -> {
            JobAssignment ja = new JobAssignment();
            ja.setJob(job);
            ja.setCandidate(candidate);
            return ja;
        }).toList();

        // Save all assignments
        List<JobAssignment> savedAssignments = jobAssignmentRepository.saveAll(assignments);

        log.info("Assigned {} candidates to job '{}' of organization {}",
                savedAssignments.size(), job.getTitle(), org.getCompanyName());

        // Map to response DTO
        return savedAssignments.stream()
                .map(Utility::mapToResponse)
                .toList();
    }

    @Transactional
    public JobAssignmentResponse updateStatusOfCandidateJob(final Long assignmentId, final JobAssignment.ApplicationStatus newStatus) {
        Organization org = getOrganizationFromAuthenticatedUser();

        JobAssignment assignment = jobAssignmentRepository.findByIdAndJobOrganizationId(assignmentId, org.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id " + assignmentId));

        assignment.setStatus(newStatus);

        log.info("Assignment {} updated to status {} by organization {}",
                assignmentId, newStatus, org.getCompanyName());

        return Utility.mapToResponse(assignment);
    }

    @Transactional(readOnly = true)
    public JobAssignmentResponse getJobAssignmentById(final Long assignmentId) {
        Organization org = getOrganizationFromAuthenticatedUser();

        JobAssignment assignment = jobAssignmentRepository.findByIdAndJobOrganizationId(assignmentId, org.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id " + assignmentId));

        return Utility.mapToResponse(assignment);
    }

    @Transactional(readOnly = true)
    public List<JobAssignmentResponse> getAllAssignmentsForJob(Long jobId) {
        Organization org = getOrganizationFromAuthenticatedUser();

        Job job = jobRepository.findByIdAndOrganizationId(jobId, org.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + jobId));

        List<JobAssignment> assignments = jobAssignmentRepository.findAllByJobId(job.getId());

        return assignments.stream()
                .map(Utility::mapToResponse)
                .toList();
    }
}
