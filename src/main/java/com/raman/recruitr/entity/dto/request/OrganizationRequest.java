package com.raman.recruitr.entity.dto.request;


import com.raman.recruitr.validation.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrganizationRequest(
        @NotBlank(message = "Company name is required", groups = OnCreate.class)
        @Size(max = 50, message = "Company name cannot exceed 50 characters")
        String companyName,

        @NotBlank(message = "Address is required", groups = OnCreate.class)
        String address,

        @NotBlank(message = "City is required", groups = OnCreate.class)
        String city,

        @Email(message = "Email should be valid")
        String email,
        String website,

        @NotBlank(message = "Org Admin email cannot be blank", groups = OnCreate.class)
        @Email(message = "Org Admin Email should be valid")
        String orgAdminEmail,

        @NotBlank(message = "Password cannot be blank", groups = OnCreate.class)
        @Size(min = 8, max = 50, message = "Minimum 8, Maximum 20 characters")
        String orgAdminPassword

) {}