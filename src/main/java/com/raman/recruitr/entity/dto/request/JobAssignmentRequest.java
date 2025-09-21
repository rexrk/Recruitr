package com.raman.recruitr.entity.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record JobAssignmentRequest(
        @NotNull @Positive Long jobId,
        @NotEmpty Set<Long> candidateIds
) {}
