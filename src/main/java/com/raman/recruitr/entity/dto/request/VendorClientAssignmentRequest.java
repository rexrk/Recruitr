package com.raman.recruitr.entity.dto.request;

import java.util.Set;

public record VendorClientAssignmentRequest(
        Long organizationId,
        Set<Long> vendorIds,
        Set<Long> clientIds
) {}