package com.raman.recruitr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("message", "Pong");
    }
}