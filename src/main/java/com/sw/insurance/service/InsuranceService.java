package com.sw.insurance.service;

import com.sw.insurance.dto.CarInsuranceDetails;
import com.sw.insurance.dto.HealthInsuranceDetails;
import com.sw.insurance.dto.InsuranceResponse;
import com.sw.insurance.dto.PetInsuranceDetails;
import com.sw.insurance.dto.VehicleInfo;
import com.sw.insurance.model.Person;
import com.sw.insurance.model.Policy;
import com.sw.insurance.model.PolicyDetails;
import com.sw.insurance.repository.PersonRepository;
import com.sw.insurance.repository.PolicyDetailsRepository;
import com.sw.insurance.repository.PolicyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InsuranceService {

    private final PersonRepository personRepository;
    private final PolicyRepository policyRepository;
    private final PolicyDetailsRepository policyDetailsRepository;
    private final RestTemplate restTemplate;
    
    @Value("${vehicle.service.url}")
    private String vehicleServiceUrl;

    @Value("${vehicle.service.path}")
    private String vehicleServicePath;

    public InsuranceService(PersonRepository personRepository, 
                          PolicyRepository policyRepository,
                          PolicyDetailsRepository policyDetailsRepository,
                          RestTemplate restTemplate) {
        this.personRepository = personRepository;
        this.policyRepository = policyRepository;
        this.policyDetailsRepository = policyDetailsRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public List<InsuranceResponse> getInsurancesByPersonalId(String personalId) {
        Person person = personRepository.findByPersonalId(personalId)
                .orElseThrow(() -> new EntityNotFoundException("Person not found with personal ID: " + personalId));

        return policyRepository.findActivePoliciesByPersonId(person.getId()).stream()
                .map(this::mapToInsuranceResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private InsuranceResponse mapToInsuranceResponse(Policy policy) {
        InsuranceResponse response = new InsuranceResponse();
        response.setProductCode(policy.getProductCode());
        response.setProductName(getProductName(policy.getProductCode()));
        response.setMonthlyPrice(policy.getMonthlyPrice());
        
        // Get policy details if they exist
        policyDetailsRepository.findByPolicyId(policy.getId()).ifPresent(details -> {
            switch (policy.getProductCode()) {
                case "CAR" -> handleCarInsurance(details, response);
                case "PET" -> handlePetInsurance(details, response);
                case "HEALTH" -> handleHealthInsurance(details, response);
                // Add more cases as needed
            }
        });
        
        return response;
    }

    private void handleCarInsurance(PolicyDetails details, InsuranceResponse response) {
        if (details.getVehicleRegistration() == null) return;
        
        try {
            String url = String.format("%s%s%s", vehicleServiceUrl, vehicleServicePath, details.getVehicleRegistration());
            VehicleInfo vehicleInfo = restTemplate.getForObject(url, VehicleInfo.class);
            
            if (vehicleInfo != null) {
                CarInsuranceDetails carDetails = new CarInsuranceDetails();
                carDetails.setRegistrationNumber(vehicleInfo.getRegistrationNumber());
                carDetails.setMake(vehicleInfo.getMake());
                carDetails.setModel(vehicleInfo.getModel());
                carDetails.setYear(vehicleInfo.getYear());
                carDetails.setColor(vehicleInfo.getColor());
                response.setDetails(carDetails);
            }
        } catch (Exception e) {
            System.err.println("Error fetching vehicle info: " + e.getMessage());
        }
    }
    
    private void handlePetInsurance(PolicyDetails details, InsuranceResponse response) {
        if (details.getPet() == null) return;
        
        PetInsuranceDetails petDetails = new PetInsuranceDetails();
        petDetails.setPetName(details.getPet().getName());
        petDetails.setSpecies(details.getPet().getSpecies());
        petDetails.setBreed(details.getPet().getBreed());
        petDetails.setBirthDate(details.getPet().getBirthDate());
        petDetails.setMicrochipNumber(details.getPet().getMicrochipNumber());
        response.setDetails(petDetails);
    }
    
    private void handleHealthInsurance(PolicyDetails details, InsuranceResponse response) {
        if (details.getHealthInfo() == null) return;
        
        HealthInsuranceDetails healthDetails = new HealthInsuranceDetails();
        
        try {
            // Convert the JSON string to a Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> healthInfo = objectMapper.readValue(
                details.getHealthInfo().toString(), 
                new TypeReference<Map<String, Object>>() {}
            );
            
            // Map common health insurance fields
            if (healthInfo.containsKey("policyNumber")) {
                healthDetails.setPolicyNumber(safeToString(healthInfo.get("policyNumber")));
            }
            if (healthInfo.containsKey("coverageType")) {
                healthDetails.setCoverageType(safeToString(healthInfo.get("coverageType")));
            }
            if (healthInfo.containsKey("primaryHolder")) {
                healthDetails.setPrimaryHolder(safeToString(healthInfo.get("primaryHolder")));
            }
            if (healthInfo.containsKey("dependents") && healthInfo.get("dependents") instanceof List) {
                healthDetails.setDependents((List<String>) healthInfo.get("dependents"));
            }
            if (healthInfo.containsKey("planType")) {
                healthDetails.setPlanType(safeToString(healthInfo.get("planType")));
            }
            if (healthInfo.containsKey("providerName")) {
                healthDetails.setProviderName(safeToString(healthInfo.get("providerName")));
            }
            if (healthInfo.containsKey("networkType")) {
                healthDetails.setNetworkType(safeToString(healthInfo.get("networkType")));
            }
            // Add more fields as needed
            
        } catch (Exception e) {
            System.err.println("Error parsing health info: " + e.getMessage());
            // Optionally log the full stack trace for debugging
            // e.printStackTrace();
        }
        
        response.setDetails(healthDetails);
    }
    
    // Helper method to safely convert objects to String
    private String safeToString(Object obj) {
        return obj != null ? obj.toString() : null;
    }
    
    private String getProductName(String productCode) {
        return switch (productCode) {
            case "CAR" -> "Car Insurance";
            case "PET" -> "Pet Insurance";
            case "HEALTH" -> "Health Insurance";
            default -> productCode;
        };
    }
}
