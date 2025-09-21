package com.raman.recruitr.controller;

import com.raman.recruitr.entity.dto.request.CandidateRequest;
import com.raman.recruitr.entity.dto.response.CandidateResponse;
import com.raman.recruitr.service.CandidateService;
import com.raman.recruitr.utils.Constants;
import com.raman.recruitr.validation.OnCreate;
import com.raman.recruitr.validation.OnUpdate;
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
@RequestMapping("/api/organization/candidates")
@RequiredArgsConstructor
@SecurityRequirement(name = Constants.SCHEME_NAME)
@Tag(name = "Candidate (Org-Admin)", description = "APIs for managing candidates. Login with org_admins credentials only.")
@PreAuthorize("hasRole('ORG_ADMIN')")
public class OrgAdminCandidateController {

    private final CandidateService candidateService;

    @PostMapping
    public ResponseEntity<CandidateResponse> createCandidate(@Validated(OnCreate.class) @RequestBody CandidateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(candidateService.createCandidate(request));
    }

    @GetMapping
    public ResponseEntity<List<CandidateResponse>> getVisibleCandidates() {
        return ResponseEntity.status(HttpStatus.OK).body(candidateService.getAllCandidates());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateResponse> updateCandidate(
            @PathVariable @Positive Long id,
            @Validated(OnUpdate.class) @RequestBody CandidateRequest request) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable @Positive Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}
