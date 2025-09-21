package com.raman.recruitr.utils;

import com.raman.recruitr.entity.Candidate;
import com.raman.recruitr.entity.Job;
import com.raman.recruitr.entity.JobAssignment;
import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.response.CandidateResponse;
import com.raman.recruitr.entity.dto.response.JobAssignmentResponse;
import com.raman.recruitr.entity.dto.response.JobResponse;
import com.raman.recruitr.entity.dto.response.OrganizationResponse;

public class Utility {

    // Helper to map entity -> response
    public static OrganizationResponse mapToResponse(Organization org) {
        return new OrganizationResponse(
                org.getId(),
                org.getCompanyName(),
                org.getEmail(),
                org.getWebsite(),
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

    public static CandidateResponse mapToResponse(Candidate c) {
        return new CandidateResponse(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getEmail(),
                c.getPhone(),
                c.getResumePath(),
                c.getPrimarySkills(),
                c.getTotalExperience(),
                c.getCurrentLocation(),
                c.getPreferredLocation(),
                c.getOrganization().getCompanyName(),
                c.getCreatedAt()
        );
    }

    public static JobAssignmentResponse mapToResponse(JobAssignment ja) {
        return new JobAssignmentResponse(
                ja.getId(),
                ja.getJob().getId(),
                ja.getJob().getTitle(),
                ja.getCandidate().getId(),
                ja.getCandidate().getFirstName() + " " + ja.getCandidate().getLastName(),
                ja.getCandidate().getEmail(),
                ja.getStatus().name()
        );
    }
}
