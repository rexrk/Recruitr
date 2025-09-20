package com.raman.recruitr.utils;

import com.raman.recruitr.entity.Job;
import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.response.JobResponse;
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

    public static JobResponse mapToResponse(Job job) {
        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getRequiredSkills(),
                job.getExperienceLevel().name(),
                job.getStatus().name(),
                job.getCreatedAt(),
                job.getOrganization().getCompanyName()
        );
    }
}
