package com.raman.recruitr.entity.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrganizationRequest(
        @NotBlank(message = "Company name is required")
        @Size(max = 50, message = "Company name cannot exceed 50 characters")
        String companyName,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "City is required")
        String city,

        @Email(message = "Email should be valid")
        String email,
        String website,

        @Email(message = "Org Admin Email should be valid")
        String orgAdminEmail,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 50, message = "Minimum 8, Maximum 20 characters")
        String orgAdminPassword

) {}