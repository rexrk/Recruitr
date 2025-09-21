package com.raman.recruitr.entity.dto.response;

// Response: details of the assignment
public record JobAssignmentResponse(
        Long id,
        Long jobId,
        String jobTitle,
        Long candidateId,
        String candidateName,
        String candidateEmail,
        String status
) {}
