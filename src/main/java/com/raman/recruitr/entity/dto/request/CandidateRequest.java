package com.raman.recruitr.entity.dto.request;

import com.raman.recruitr.validation.OnCreate;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.util.Set;

public record CandidateRequest(
    @NotBlank(groups = OnCreate.class) String firstName,
    @NotBlank(groups = OnCreate.class) String lastName,
    @Email String email,
    @Size(min = 10, max = 10) String phone,
    @NotBlank(groups = OnCreate.class) String resumeReferenceUrl,
    @NotEmpty(groups = OnCreate.class) Set<String> primarySkills,
    @Digits(integer = 2, fraction = 1) @Range(min = 1, max = 50) Double totalExperience,
    @NotBlank(groups = OnCreate.class) String currentLocation,
    @NotBlank(groups = OnCreate.class) String preferredLocation
) {}