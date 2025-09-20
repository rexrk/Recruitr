package com.raman.recruitr.entity.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

public record JobResponse(
        Long id,
        String title,
        String description,
        Set<String> requiredSkills,
        String experienceLevel,
        String status,
        LocalDateTime createdAt,
        String organizationName
) {}