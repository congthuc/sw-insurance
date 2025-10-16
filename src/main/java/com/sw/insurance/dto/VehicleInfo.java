package com.sw.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleInfo {
    private String registrationNumber;
    private String vin;
    private String make;
    private String model;
    private Integer year;
    private String color;
}
