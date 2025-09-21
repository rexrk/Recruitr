package com.raman.recruitr.entity.dto.request;

import com.raman.recruitr.exception.IllegalArgumentException;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record VendorClientAssignmentRequest(
        @NotNull Long organizationId,
        Set<Long> vendorIds,
        Set<Long> clientIds
) {
    public VendorClientAssignmentRequest {
        // Validation 1: At least one set must be provided (non-silent
        if ((vendorIds == null || vendorIds.isEmpty()) &&
                (clientIds == null || clientIds.isEmpty())) {
            throw new IllegalArgumentException("At least one of vendorIds or clientIds must be provided.");
        }

        // Validation 2: Silently remove any IDs that match the organizationId (silent)
        if (vendorIds != null) {
            vendorIds.remove(organizationId);
        }
        if (clientIds != null) {
            clientIds.remove(organizationId);
        }
    }
}