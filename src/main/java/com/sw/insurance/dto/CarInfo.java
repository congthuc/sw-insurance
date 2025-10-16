package com.sw.insurance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarInfo {
    private String registrationNumber;
    private String make;
    private String model;
    private Integer year;
    private String color;
}
