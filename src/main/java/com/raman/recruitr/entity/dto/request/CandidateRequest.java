package com.raman.recruitr.entity.dto.request;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

public record CandidateRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    @Size(min = 10, max = 10) String phone,
    @NotBlank String resumeReferenceUrl,
    @NotEmpty @NotNull Set<String> primarySkills,
    @Digits(integer = 2, fraction = 1) @Range(min = 1, max = 50) Double totalExperience,
    @NotBlank String currentLocation,
    @NotBlank String preferredLocation
) {}