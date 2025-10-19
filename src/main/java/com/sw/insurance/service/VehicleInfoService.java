package com.sw.insurance.service;

import com.sw.insurance.dto.CarInsuranceDetails;
import com.sw.insurance.dto.VehicleInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class VehicleInfoService {

    private final RestTemplate restTemplate;
    private final String vehicleServiceUrl;
    private final String vehicleServicePath;

    public VehicleInfoService(
            RestTemplate restTemplate,
            @Value("${vehicle.service.url}") String vehicleServiceUrl,
            @Value("${vehicle.service.path}") String vehicleServicePath) {
        this.restTemplate = restTemplate;
        this.vehicleServiceUrl = vehicleServiceUrl;
        this.vehicleServicePath = vehicleServicePath;
    }

    /**
     * Fetches vehicle information from the external vehicle service
     * @param registrationNumber The vehicle registration number
     * @return CarInsuranceDetails containing the vehicle information, or null if not found
     */
    public CarInsuranceDetails getVehicleInfo(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            return null;
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl(vehicleServiceUrl)
                    .path(vehicleServicePath)
                    .path(registrationNumber)
                    .toUriString();

            VehicleInfo vehicleInfo = restTemplate.getForObject(url, VehicleInfo.class);
            
            if (vehicleInfo != null) {
                return mapToCarInsuranceDetails(vehicleInfo);
            }
        } catch (Exception e) {
            // Log the error and return null
            System.err.println("Error fetching vehicle info for " + registrationNumber + ": " + e.getMessage());
        }
        
        return null;
    }

    private CarInsuranceDetails mapToCarInsuranceDetails(VehicleInfo vehicleInfo) {
        CarInsuranceDetails details = new CarInsuranceDetails();
        details.setRegistrationNumber(vehicleInfo.getRegistrationNumber());
        details.setMake(vehicleInfo.getMake());
        details.setModel(vehicleInfo.getModel());
        details.setYear(vehicleInfo.getYear());
        details.setColor(vehicleInfo.getColor());
        return details;
    }
}
