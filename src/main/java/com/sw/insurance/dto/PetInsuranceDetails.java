package com.sw.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PetInsuranceDetails extends InsuranceDetails {
    private String petName;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private String microchipNumber;
}
