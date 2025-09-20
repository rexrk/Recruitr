package com.raman.recruitr.controller;

import com.raman.recruitr.entity.dto.request.OrganizationRequest;
import com.raman.recruitr.entity.dto.request.VendorClientAssignmentRequest;
import com.raman.recruitr.entity.dto.response.OrganizationResponse;
import com.raman.recruitr.entity.dto.response.VendorClientProjectionResponse;
import com.raman.recruitr.entity.dto.response.VendorClientResponse;
import com.raman.recruitr.service.OrganizationService;
import com.raman.recruitr.utils.Constants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations")
@SecurityRequirement(name = Constants.SCHEME_NAME)
@Tag(name = "Admin", description = "APIs for managing organizations. Login with admin credentials to access.")
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrganizationResponse> create(@RequestBody OrganizationRequest request) {
        OrganizationResponse response = organizationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrganizationResponse>> getAll() {
        List<OrganizationResponse> response = organizationService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<OrganizationResponse> getById(@PathVariable Long id) {
        OrganizationResponse response = organizationService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrganizationResponse> update(@PathVariable Long id,
                                                       @RequestBody OrganizationRequest request) {
        OrganizationResponse response = organizationService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/vendors-and-clients/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorClientResponse> updateVendorsAndClients(@RequestBody VendorClientAssignmentRequest request) {
        VendorClientResponse response = organizationService.assignClientsAndVendors(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/vendors-and-clients")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<VendorClientProjectionResponse> fetchVendorsAndClients(@PathVariable Long id) {
        VendorClientProjectionResponse response = organizationService.getVendorsAndClientsByOrganizationId(id);
        return ResponseEntity.ok(response);
    }



}
