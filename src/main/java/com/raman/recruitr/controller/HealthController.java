package com.raman.recruitr.controller;

import com.raman.recruitr.utils.Constants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@SecurityRequirement(name=Constants.SCHEME_NAME)
public class HealthController {

    @PreAuthorize("hasAnyRole('ORG_ADMIN', 'ADMIN', 'USER')")
    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("message", "Pong");
    }
}