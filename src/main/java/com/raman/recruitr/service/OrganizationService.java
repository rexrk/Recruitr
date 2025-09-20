package com.raman.recruitr.service;

import com.raman.recruitr.entity.AppUser;
import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.request.OrganizationRequest;
import com.raman.recruitr.entity.dto.request.VendorClientAssignmentRequest;
import com.raman.recruitr.entity.dto.response.OrganizationProjection;
import com.raman.recruitr.entity.dto.response.OrganizationResponse;
import com.raman.recruitr.entity.dto.response.VendorClientProjectionResponse;
import com.raman.recruitr.entity.dto.response.VendorClientResponse;
import com.raman.recruitr.exception.ResourceNotFoundException;
import com.raman.recruitr.repository.OrganizationRepository;
import com.raman.recruitr.utils.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;

    private Organization helperFindById(final Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id " + id));
    }

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
        return new OrganizationResponse(
                saved.getId(),
                saved.getCompanyName(),
                saved.getEmail(),
                saved.getCity(),
                saved.getStatus().name(),
                saved.getCreatedAt(),
                saved.getAccountManager().getUsername()
        );
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


    public OrganizationResponse update(final Long id, final OrganizationRequest request) {
        log.info("[OrganizationService] Updating organization with id {}", id);
        Organization org = helperFindById(id);

        if (request.companyName() != null) org.setCompanyName(request.companyName());
        if (request.address() != null) org.setAddress(request.address());
        if (request.city() != null) org.setCity(request.city());
        if (request.email() != null) org.setEmail(request.email());
        if (request.website() != null) org.setWebsite(request.website());

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
            validVendors.addAll(organizationRepository.findAllById(request.vendorIds()));
            org.getVendors().addAll(validVendors); // only existing vendors are added
            log.info("Assigned {} vendors to organization {}", validVendors.size(), org.getCompanyName());
        }

        // Assigning Cleitns
        if (request.clientIds() != null && !request.clientIds().isEmpty()) {
            validClients.addAll(organizationRepository.findAllById(request.clientIds()));
            for (Organization client : validClients) {
                client.getVendors().add(org); // add this org as vendor to valid clients
            }
            organizationRepository.saveAll(validClients); // persist client side
            log.info("Assigned organization {} as vendor to {} clients", org.getCompanyName(), validClients.size());
        }

        organizationRepository.save(org);
        log.info("Organization {} updated successfully with assigned clients/vendors", org.getCompanyName());

        // showing orgAdminUsername is redundant ...
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
}
