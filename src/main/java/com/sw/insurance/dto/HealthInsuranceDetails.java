package com.sw.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO containing health insurance specific details.
 * This class extends InsuranceDetails to provide health insurance specific information.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class HealthInsuranceDetails extends InsuranceDetails {
    
    /**
     * The unique policy number for the health insurance.
     */
    private String policyNumber;
    
    /**
     * Type of health coverage (e.g., INDIVIDUAL, FAMILY, PARENT_CHILD, etc.).
     */
    private String coverageType;
    
    /**
     * Name of the primary policy holder.
     */
    private String primaryHolder;
    
    /**
     * List of dependents covered under this policy.
     */
    private List<String> dependents;
    
    /**
     * Type of health plan (e.g., BASIC, STANDARD, PREMIUM).
     */
    private String planType;
    
    /**
     * Start date of the coverage in ISO-8601 format (yyyy-MM-dd).
     */
    private String startDate;
    
    /**
     * End date of the coverage in ISO-8601 format (yyyy-MM-dd).
     */
    private String endDate;
    
    /**
     * Name of the insurance provider.
     */
    private String providerName;
    
    /**
     * Network type (e.g., PPO, HMO, EPO).
     */
    private String networkType;
    
    /**
     * Annual deductible amount.
     */
    private Double annualDeductible;
    
    /**
     * Out-of-pocket maximum amount.
     */
    private Double outOfPocketMax;
    
    /**
     * Co-pay amount for primary care visits.
     */
    private Double primaryCareCopay;
    
    /**
     * Co-pay amount for specialist visits.
     */
    private Double specialistCopay;
    
    /**
     * Co-pay amount for emergency room visits.
     */
    private Double emergencyRoomCopay;
    
    /**
     * Co-pay amount for urgent care visits.
     */
    private Double urgentCareCopay;
    
    /**
     * Co-insurance percentage for hospital stays.
     */
    private Integer hospitalCoinsurance;
    
    /**
     * Co-insurance percentage for prescription drugs.
     */
    private Integer prescriptionCoinsurance;
    
    /**
     * Indicates if preventive care is covered at 100%.
     */
    private Boolean preventiveCareCovered;
    
    /**
     * Indicates if dental coverage is included.
     */
    private Boolean includesDental;
    
    /**
     * Indicates if vision coverage is included.
     */
    private Boolean includesVision;
    
    /**
     * Indicates if mental health services are covered.
     */
    private Boolean includesMentalHealth;
    
    /**
     * Indicates if maternity care is covered.
     */
    private Boolean includesMaternity;
    
    /**
     * Indicates if pre-existing conditions are covered.
     */
    private Boolean coversPreexistingConditions;
    
    /**
     * Additional notes or special conditions.
     */
    private String notes;
}
