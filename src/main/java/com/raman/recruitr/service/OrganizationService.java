package com.raman.recruitr.service;

import com.raman.recruitr.entity.AppUser;
import com.raman.recruitr.entity.Job;
import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.request.JobRequest;
import com.raman.recruitr.entity.dto.request.OrganizationRequest;
import com.raman.recruitr.entity.dto.request.VendorClientAssignmentRequest;
import com.raman.recruitr.entity.dto.response.*;
import com.raman.recruitr.exception.ResourceNotFoundException;
import com.raman.recruitr.repository.JobRepository;
import com.raman.recruitr.repository.OrganizationRepository;
import com.raman.recruitr.utils.Constants;
import com.raman.recruitr.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JobRepository jobRepository;

    // Helper Functions ======================================================================================
    private Organization helperFindById(final Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id " + id));
    }

    private void updateHelper(OrganizationRequest request, Organization org) {
        if (request.companyName() != null) org.setCompanyName(request.companyName());
        if (request.address() != null) org.setAddress(request.address());
        if (request.city() != null) org.setCity(request.city());
        if (request.email() != null) org.setEmail(request.email());
        if (request.website() != null) org.setWebsite(request.website());
        org.setStatus(Organization.Status.ACTIVE);

        // Update or create account manager only if both email and password are provided
        if (request.orgAdminEmail() != null && request.orgAdminPassword() != null) {
            AppUser newAccountManager = AppUser.builder()
                    .username(request.orgAdminEmail())
                    .password(passwordEncoder.encode(request.orgAdminPassword()))
                    .role(AppUser.Role.ORG_ADMIN)
                    .build();
            log.info("[OrganizationService] Updated account manager to username {}", request.orgAdminEmail());
            org.setAccountManager(newAccountManager);
        }
    }

    private static Organization getOrganizationFromAuthenticatedUser() {
        AppUser orgAdmin = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (orgAdmin.getOrganization() == null) throw new ResourceNotFoundException("No Organization is assigned to your account");
        return orgAdmin.getOrganization();
    }

    //  Admin services ======================================================================================

    public OrganizationResponse create(OrganizationRequest request) {
        log.info("[OrganizationService] Create new organization request received for {}", request.companyName());

        // Create an account manager
        AppUser accountManager = AppUser.builder()
                .username(request.orgAdminEmail())
                .password(passwordEncoder.encode(request.orgAdminPassword()))
                .role(AppUser.Role.ORG_ADMIN)
                .build();

        // Map org dto -> org obj
        Organization org = new Organization();
        org.setCompanyName(request.companyName());
        org.setAddress(request.address());
        org.setCity(request.city());
        org.setEmail(request.email());
        org.setWebsite(request.website());
        org.setAccountManager(accountManager);

        // save to databse
        // maybe check if exists first
        Organization saved = organizationRepository.save(org);
        log.info("[OrganizationService] Organization Created successfully for {}", request.companyName());

        // return response obj
        return Utility.mapToResponse(saved);
    }

    public List<OrganizationResponse> getAll() {
        log.info("[OrganizationService] Fetching all organizations");
        return organizationRepository.findAll().stream()
                .map(Utility::mapToResponse)
                .toList();
    }

    public OrganizationResponse getById(Long id) {
        log.info("[OrganizationService] Fetching organization with id {}", id);
        return Utility.mapToResponse(helperFindById(id));
    }

    @Transactional
    public OrganizationResponse update(final Long id, final OrganizationRequest request) {
        log.info("[OrganizationService] Updating organization with id {}", id);
        Organization org = helperFindById(id);

        updateHelper(request, org);

        Organization saved = organizationRepository.save(org);
        log.info("Organization: {} updated successfully", saved.getCompanyName());
        return Utility.mapToResponse(saved);
    }

    public void delete(final Long id) {
        Organization org = helperFindById(id);
        org.setStatus(Organization.Status.INACTIVE);
        log.info("Organization: {} marked as INACTIVE", org.getCompanyName());
        organizationRepository.save(org);
    }

    public VendorClientResponse assignClientsAndVendors(VendorClientAssignmentRequest request) {
        Organization org = helperFindById(request.organizationId());
        log.info("Assigning clients/vendors for organization id {}", request.organizationId());
        List<Organization> validVendors = new ArrayList<>();
        List<Organization> validClients = new ArrayList<>();

        // Assigning Vendors
        if (request.vendorIds() != null && !request.vendorIds().isEmpty()) {
            validVendors.addAll(organizationRepository
                    .findAllById(request.vendorIds())
                    .stream().filter(vendor -> vendor.getStatus() == Organization.Status.ACTIVE).toList()
            );
            org.getVendors().addAll(validVendors); // only existing vendors are added
            log.info("Assigned {} vendors to organization {}", validVendors.size(), org.getCompanyName());
        }

        // Assigning Cleitns
        if (request.clientIds() != null && !request.clientIds().isEmpty()) {
            validClients.addAll(organizationRepository
                    .findAllById(request.clientIds())
                    .stream().filter(client -> client.getStatus() == Organization.Status.ACTIVE).toList()
            );
            for (Organization client : validClients) {
                client.getVendors().add(org); // add this org as vendor to valid clients
            }
            organizationRepository.saveAll(validClients); // persist client side
            log.info("Assigned organization {} as vendor to {} clients", org.getCompanyName(), validClients.size());
        }

        organizationRepository.save(org);
        log.info("Organization {} updated successfully with assigned clients/vendors", org.getCompanyName());

        if(BooleanUtils.isFalse(validVendors.isEmpty())
                && BooleanUtils.isFalse(validClients.isEmpty())) { org.setStatus(Organization.Status.ACTIVE); }

        return new VendorClientResponse(
                validVendors.stream().map(Utility::mapToResponse).toList(),
                validClients.stream().map(Utility::mapToResponse).toList()
        );
    }

    public VendorClientProjectionResponse getVendorsAndClientsByOrganizationId(final Long id) {
        List<OrganizationProjection> vendors = organizationRepository.findVendorsByOrgId(id);
        List<OrganizationProjection> clients = organizationRepository.findClientsByOrgId(id);

        log.info("Org {} has {} vendors and {} clients", id, vendors.size(), clients.size());

        return new VendorClientProjectionResponse(vendors, clients);
    }

    // Org Admin services ======================================================================================

    @Transactional
    public OrganizationResponse updateMyOrganization(OrganizationRequest request) {
        // Fetch logged-in ORG_ADMIN
        Organization org = getOrganizationFromAuthenticatedUser();

        updateHelper(request, org);

        Organization saved = organizationRepository.save(org);
        log.info("Your Organization updated successfully");

        return Utility.mapToResponse(saved);
    }

    public void uploadLogo(MultipartFile file) {
        if (file.getContentType() == null ||
            !(file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg"))) {
            throw new IllegalArgumentException("Only PNG or JPG images are allowed");
        }

        Organization org = getOrganizationFromAuthenticatedUser();

        try {
            // Ensure folder exists
            Files.createDirectories(Paths.get(Constants.LOGO_DIR));

            String ext = "";
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }

            String newFileName = "logo_org" + org.getId() + ext;

            Path filePath = Paths.get(Constants.LOGO_DIR, newFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            org.setLogo(newFileName);
            organizationRepository.save(org);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload logo", e);
        }
    }

    public Resource getLogoFile() {
        Organization org = getOrganizationFromAuthenticatedUser();

        try {
            Path filePath = Paths.get(Constants.LOGO_DIR, org.getLogo());
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Logo file not found");
            }
            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to read logo file", e);
        }
    }

    public OrganizationResponse fetchMyOrganization() {
        return Utility.mapToResponse(getOrganizationFromAuthenticatedUser());

    }

    public VendorClientProjectionResponse getMyVendorsAndClients() {
        return getVendorsAndClientsByOrganizationId(getOrganizationFromAuthenticatedUser().getId());
    }

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
        log.info("Job '{}' created for organization {}",
                saved.getTitle(), org.getCompanyName());

        return Utility.mapToResponse(saved);
    }

    /**
     * Update a job: OrgAdmin can only close jobs from their organization
     */
    @Transactional
    public void closeJob(final Long jobId) {
        Organization org = getOrganizationFromAuthenticatedUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + jobId));

        if (BooleanUtils.isFalse(job.getOrganization().getId().equals(org.getId()))) {
            throw new SecurityException("Job not found with id" + jobId);
        }

        job.setStatus(Job.JobStatus.CLOSED);
        log.info("Job '{}' closed for organization {}",
                job.getTitle(), org.getCompanyName());

    }

}
