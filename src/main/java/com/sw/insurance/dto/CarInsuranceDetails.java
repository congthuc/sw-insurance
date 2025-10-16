package com.sw.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class CarInsuranceDetails extends InsuranceDetails {
    private String registrationNumber;
    private String make;
    private String model;
    private Integer year;
    private String color;
}
