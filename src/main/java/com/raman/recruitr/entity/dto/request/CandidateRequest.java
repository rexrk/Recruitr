package com.raman.recruitr.entity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record CandidateRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @Email String email,
    String phone,
    Set<String> primarySkills,
    Double totalExperience,
    String currentLocation,
    String preferredLocation
) {}