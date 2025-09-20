package com.raman.recruitr.entity.dto.response;

import java.time.LocalDateTime;

public record OrganizationResponse(
    Long id,
    String companyName,
    String email,
    String city,
    String status,
    LocalDateTime createdAt,
    String orgAdminUsername
) {}
