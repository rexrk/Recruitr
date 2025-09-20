package com.raman.recruitr.utils;

import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.response.OrganizationResponse;

public class Utility {

    // Helper to map entity -> response
    public static OrganizationResponse mapToResponse(Organization org) {
        return new OrganizationResponse(
                org.getId(),
                org.getCompanyName(),
                org.getEmail(),
                org.getCity(),
                org.getStatus().name(),
                org.getCreatedAt(),
                org.getAccountManager().getUsername()
        );
    }
}
