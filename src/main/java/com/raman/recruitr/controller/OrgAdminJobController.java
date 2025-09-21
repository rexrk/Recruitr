package com.raman.recruitr.controller;

import com.raman.recruitr.entity.JobAssignment;
import com.raman.recruitr.entity.dto.request.JobAssignmentRequest;
import com.raman.recruitr.entity.dto.request.JobRequest;
import com.raman.recruitr.entity.dto.response.JobAssignmentResponse;
import com.raman.recruitr.entity.dto.response.JobResponse;
import com.raman.recruitr.service.JobService;
import com.raman.recruitr.utils.Constants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organization/jobs")
@SecurityRequirement(name = Constants.SCHEME_NAME)
@Tag(name = "Jobs (Org-Admin)", description = "APIs for managing jobs. Login with org_admins credentials only.")
@PreAuthorize("hasRole('ORG_ADMIN')")
public class OrgAdminJobController {

    private final JobService jobService;

    /**
     * Create a new job for the logged-in OrgAdmin's organization
     */
    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(jobService.createJob(request));
    }

     /**
     * Find all jobs inside logged-in OrgAdmin's organization
     */
    @GetMapping
    public ResponseEntity<List<JobResponse>> fetchAllJobs() {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs());
    }

     /**
     * Find one job inside logged-in OrgAdmin's organization
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> fetchJobById(@PathVariable @Positive Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getJobById(id));
    }

    /**
     * Close an existing job (status = CLOSED)
     */
    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeJob(@PathVariable @Positive Long id) {
        jobService.closeJob(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Assign job to candidates (own + Vendors)
     */
    @PostMapping("/assigns")
    public ResponseEntity<List<JobAssignmentResponse>> assignCandidates( @Valid @RequestBody JobAssignmentRequest request) {
        return ResponseEntity.ok(jobService.assignCandidatesToJob(request));
    }

    @GetMapping("/{id}/assigns")
    public ResponseEntity<List<JobAssignmentResponse>> fetchAllAssignmentsForJob(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(jobService.getAllAssignmentsForJob(id));
    }

    @GetMapping("/assigns/{id}")
    public ResponseEntity<JobAssignmentResponse> fetchAssignment(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(jobService.getJobAssignmentById(id));
    }

    @PatchMapping("/assigns/{id}/status")
    public ResponseEntity<JobAssignmentResponse> updateAssignmentStatus(@PathVariable @Positive Long id,
            @RequestParam JobAssignment.ApplicationStatus status) {
        return ResponseEntity.ok(jobService.updateStatusOfCandidateJob(id, status));
    }

}
