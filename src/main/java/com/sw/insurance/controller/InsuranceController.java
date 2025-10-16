package com.sw.insurance.controller;

import com.sw.insurance.dto.InsuranceResponse;
import com.sw.insurance.service.InsuranceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @GetMapping("/customer/{personalId}")
    public ResponseEntity<List<InsuranceResponse>> getInsurancesByPersonalId(
            @PathVariable String personalId) {
        
        List<InsuranceResponse> insurances = insuranceService.getInsurancesByPersonalId(personalId);
        return ResponseEntity.ok(insurances);
    }
}
