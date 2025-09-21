package com.raman.recruitr.service;

import com.raman.recruitr.entity.AppUser;
import com.raman.recruitr.entity.Candidate;
import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.request.CandidateRequest;
import com.raman.recruitr.entity.dto.response.*;
import com.raman.recruitr.exception.ResourceNotFoundException;
import com.raman.recruitr.repository.CandidateRepository;
import com.raman.recruitr.repository.OrganizationRepository;
import com.raman.recruitr.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final OrganizationRepository organizationRepository;

    // Helper Functions ======================================================================================
    // Repeating this code as it has authentication object
    private static Organization getOrganizationFromAuthenticatedUser() {
        AppUser orgAdmin = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (orgAdmin.getOrganization() == null) throw new ResourceNotFoundException("No Organization is assigned to your account");
        return orgAdmin.getOrganization();
    }

    /**
     * Create candidate for logged-in OrgAdmin's organization
     */
    public CandidateResponse createCandidate(final CandidateRequest request) {
        Organization org = getOrganizationFromAuthenticatedUser();

        Candidate candidate = Candidate.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .resumePath(request.resumeReferenceUrl())
                .primarySkills(request.primarySkills())
                .totalExperience(request.totalExperience())
                .currentLocation(request.currentLocation())
                .preferredLocation(request.preferredLocation())
                .organization(org)
                .build();

        Candidate saved = candidateRepository.save(candidate);
        log.info("Candidate '{}' created by organization {}", saved.getEmail(), org.getCompanyName());

        return Utility.mapToResponse(saved);
    }

    /**
     * Create candidate for logged-in OrgAdmin's organization
     */
    @Transactional
    public CandidateResponse updateCandidate(final Long candidateId, final CandidateRequest request) {
        Organization org = getOrganizationFromAuthenticatedUser();

        Candidate candidate = candidateRepository.findByIdAndOrganizationId(candidateId, org.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id " + candidateId));

        // Update only provided fields (partial update)
        if (request.firstName() != null) candidate.setFirstName(request.firstName());
        if (request.lastName() != null) candidate.setLastName(request.lastName());
        if (request.email() != null) candidate.setEmail(request.email());
        if (request.phone() != null) candidate.setPhone(request.phone());
        if (request.resumeReferenceUrl() != null) candidate.setResumePath(request.resumeReferenceUrl());
        if (request.primarySkills() != null) candidate.setPrimarySkills(request.primarySkills());
        if (request.totalExperience() != null) candidate.setTotalExperience(request.totalExperience());
        if (request.currentLocation() != null) candidate.setCurrentLocation(request.currentLocation());
        if (request.preferredLocation() != null) candidate.setPreferredLocation(request.preferredLocation());

        Candidate saved = candidateRepository.save(candidate);
        log.info("Candidate '{}' updated by organization {}", saved.getEmail(), org.getCompanyName());

        return Utility.mapToResponse(saved);
    }

    /**
     * List candidates visible to the organization (own + vendor candidates)
     */
    // Todo: apply filters
    public List<CandidateResponse> getAllCandidates(final int page, final int size) {
        Organization org = getOrganizationFromAuthenticatedUser();
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

        // 1️⃣ Own candidates (paged)
        Page<Candidate> ownCandidatesPage = candidateRepository.findAllByOrganizationId(org.getId(), pageable);
        List<Candidate> allCandidates = new ArrayList<>(ownCandidatesPage.getContent());
        long ownCount = allCandidates.size();

        log.info("Candidates found in '{}' organization: {}", org.getCompanyName(), ownCount);

        // 2️⃣ Candidates from vendor organizations (paged separately if needed)
        Set<Long> vendorIds = organizationRepository.findVendorIdsByOrganizationId(org.getId());
        if (!vendorIds.isEmpty()) {
            List<Candidate> vendorCandidates = candidateRepository.findAllByOrganizationIds(vendorIds, pageable).getContent();
            allCandidates.addAll(vendorCandidates);
            log.info("Candidates found in vendor organizations: {}", vendorCandidates.size());
        }

        return allCandidates.stream()
                .map(Utility::mapToResponse)
                .toList();
    }


    @Transactional
    public void deleteCandidate(final Long candidateId) {
        Organization org = getOrganizationFromAuthenticatedUser();

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id " + candidateId));

        // Ensure candidate belongs to logged-in organization
        if (!candidate.getOrganization().getId().equals(org.getId())) {
            throw new AuthorizationDeniedException("Not allowed to delete this candidate");
        }

        candidateRepository.delete(candidate);
        log.info("Candidate '{}' deleted by organization {}", candidate.getEmail(), org.getCompanyName());
    }

    @Transactional(readOnly = true)
    public List<CandidateResponse> searchCandidates(Set<String> skills) {
        Organization org = getOrganizationFromAuthenticatedUser();
        Set<String> normalizedSkills = skills.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .collect(Collectors.toSet());

        return candidateRepository.searchCandidatesBySkills(normalizedSkills, org.getId())
                .stream()
                .map(Utility::mapToResponse)
                .toList();
    }

}
