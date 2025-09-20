package com.raman.recruitr.entity.dto.response;

import java.util.List;

public record VendorClientResponse(
        List<OrganizationResponse> vendorCompaniesName,
        List<OrganizationResponse> clientCompaniesName
) {}
