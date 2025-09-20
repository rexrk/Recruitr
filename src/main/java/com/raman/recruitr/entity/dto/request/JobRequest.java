package com.raman.recruitr.entity.dto.request;

import com.raman.recruitr.entity.Job;
import jakarta.validation.constraints.*;

import java.util.Set;

public record JobRequest(
    @NotBlank String title,
    @NotBlank @Size(min = 50) String description,
    @NotEmpty Set<String> requiredSkills,
    @NotNull Job.ExperienceLevel experienceLevel
) {}
