package com.raman.recruitr.controller;

import com.raman.recruitr.entity.dto.request.OrganizationRequest;
import com.raman.recruitr.entity.dto.request.VendorClientAssignmentRequest;
import com.raman.recruitr.entity.dto.response.OrganizationResponse;
import com.raman.recruitr.entity.dto.response.VendorClientProjectionResponse;
import com.raman.recruitr.entity.dto.response.VendorClientResponse;
import com.raman.recruitr.service.OrganizationService;
import com.raman.recruitr.utils.Constants;
import com.raman.recruitr.validation.OnCreate;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organizations")
@SecurityRequirement(name = Constants.SCHEME_NAME)
@Tag(name = "Organizations (Admin)", description = "APIs for managing organizations. Login with admin credentials to access.")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationResponse> create(@Validated(OnCreate.class) @RequestBody OrganizationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAll() {
        return ResponseEntity.ok(organizationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponse> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(organizationService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        organizationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponse> update(@PathVariable @Positive Long id,
                                                       @Valid @RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.update(id, request));
    }

    @PostMapping("/vendors-and-clients/assign")
    public ResponseEntity<VendorClientResponse> updateVendorsAndClients(@Valid @RequestBody VendorClientAssignmentRequest request) {
        return ResponseEntity.ok(organizationService.assignClientsAndVendors(request));
    }

    @GetMapping("/{id}/vendors-and-clients")
    public ResponseEntity<VendorClientProjectionResponse> fetchVendorsAndClients(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(organizationService.getVendorsAndClientsByOrganizationId(id));
    }

}
