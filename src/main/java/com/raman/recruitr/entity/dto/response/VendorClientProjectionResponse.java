package com.raman.recruitr.entity.dto.response;

import java.util.List;

public record VendorClientProjectionResponse(
        List<OrganizationProjection> vendors,
        List<OrganizationProjection> clients
) {}