package com.raman.recruitr.controller;

import com.raman.recruitr.entity.dto.request.OrganizationRequest;
import com.raman.recruitr.entity.dto.response.OrganizationResponse;
import com.raman.recruitr.entity.dto.response.VendorClientProjectionResponse;
import com.raman.recruitr.service.OrganizationService;
import com.raman.recruitr.utils.Constants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/organization")
@SecurityRequirement(name = Constants.SCHEME_NAME)
@Tag(name = "Organization (Org-Admin)", description = "APIs for managing self organization. Login with org admins credentials only.")
@PreAuthorize("hasRole('ORG_ADMIN')")
public class OrgAdminOrganizationController {

    private final OrganizationService organizationService;

    @PutMapping
    public ResponseEntity<OrganizationResponse> updateMyOrganization(@RequestBody OrganizationRequest request) {
        return ResponseEntity.ok(organizationService.updateMyOrganization(request));
    }

    @GetMapping
    public ResponseEntity<OrganizationResponse> fetchMyOrganization() {
        return ResponseEntity.ok(organizationService.fetchMyOrganization());
    }

    @GetMapping("/vendors-and-clients")
    public ResponseEntity<VendorClientProjectionResponse> fetchMyVendorsAndClients() {
        return ResponseEntity.ok(organizationService.getMyVendorsAndClients());
    }

    @PostMapping(value = "/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadLogo(
            @Valid @RequestParam() MultipartFile file) {
        organizationService.uploadLogo(file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/logo")
    public ResponseEntity<Resource> getLogo() {
        Resource resource = organizationService.getLogoFile();

        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(resource.getFile().toPath());
        } catch (IOException ignored) {}

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
