package com.medilabo.risk_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.risk_service.services.RiskLevelService;

@RestController
public class RiskLevelController {
    @Autowired
    private RiskLevelService riskLevelService;
    
    @GetMapping("/risk-level/{patientId}")
    public ResponseEntity<String> getRiskLevel(@PathVariable Long patientId) {
        return ResponseEntity.ok(riskLevelService.getRiskLevel(patientId));
    }
}
