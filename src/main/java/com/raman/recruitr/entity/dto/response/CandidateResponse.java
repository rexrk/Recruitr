package com.raman.recruitr.entity.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record CandidateResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phone,
    String resumeReferenceUrl,
    Set<String> primarySkills,
    Double totalExperience,
    String currentLocation,
    String preferredLocation,
    String organizationName,
    LocalDateTime createdAt
) {}