package com.raman.recruitr.controller;

import com.raman.recruitr.entity.AppUser;
import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.exception.ResourceNotFoundException;
import com.raman.recruitr.service.ReportingService;
import com.raman.recruitr.utils.Constants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@SecurityRequirement(name=Constants.SCHEME_NAME)
@RequiredArgsConstructor
@RequestMapping("/metrics")
@PreAuthorize("hasRole('ORG_ADMIN')")
public class MetricsController {

    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ADMIN', 'USER')")
    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("message", "Pong");
    }

    private final ReportingService reportingService;

    private static Organization getOrganizationFromAuthenticatedUser() {
        AppUser orgAdmin = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (orgAdmin.getOrganization() == null) throw new ResourceNotFoundException("No Organization is assigned to your account");
        return orgAdmin.getOrganization();
    }

    @GetMapping("/job-fill-rate")
    public ResponseEntity<Double> getJobFillRate() {
        Organization org = getOrganizationFromAuthenticatedUser();
        double fillRate = reportingService.getJobFillRate(org.getId());
        return ResponseEntity.ok(fillRate);
    }
}