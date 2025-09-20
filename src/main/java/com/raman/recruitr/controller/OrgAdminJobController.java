package com.raman.recruitr.controller;

import com.raman.recruitr.entity.dto.request.JobRequest;
import com.raman.recruitr.entity.dto.response.JobResponse;
import com.raman.recruitr.service.OrganizationService;
import com.raman.recruitr.utils.Constants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organization/jobs")
@SecurityRequirement(name = Constants.SCHEME_NAME)
@Tag(name = "Jobs (Org-Admin)", description = "APIs for managing jobs. Login with org_admins credentials only.")
@PreAuthorize("hasRole('ORG_ADMIN')")
public class OrgAdminJobController {

    private final OrganizationService organizationService;

    /**
     * Create a new job for the logged-in OrgAdmin's organization
     */
    @PostMapping
    public ResponseEntity<JobResponse> createJob(@Valid @RequestBody JobRequest request) {
        JobResponse response = organizationService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Close an existing job (status = CLOSED)
     */
    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeJob(@PathVariable Long id) {
        organizationService.closeJob(id);
        return ResponseEntity.noContent().build();
    }


}
