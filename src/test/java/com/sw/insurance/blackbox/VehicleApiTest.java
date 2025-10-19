package com.sw.insurance.blackbox;

import com.sw.insurance.dto.CarInsuranceDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Blackbox test that makes direct HTTP calls to the vehicle information service.
 * Assumes the vehicle information service is running on localhost:8080.
 */
public class VehicleApiTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String API_PATH = "/api/v1/vehicles/";
    
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    void getVehicleInfo_shouldReturnValidResponse_whenRegistrationExists() {
        // Given - Using a test registration that should exist in the test environment
        String registrationNumber = "ABC123";
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(API_PATH)
                .path(registrationNumber)
                .toUriString();

        // Verify the service is up and responding
        String healthUrl = BASE_URL + "/actuator/health";
        ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Vehicle service health check failed");
        assertTrue(response.getBody() != null && response.getBody().contains("UP"), 
            "Vehicle service should be UP");

        // When
        ResponseEntity<CarInsuranceDetails> vehicleResponse = restTemplate.getForEntity(
            url, 
            CarInsuranceDetails.class
        );

        // Then
        assertEquals(HttpStatus.OK, vehicleResponse.getStatusCode(), "HTTP status should be 200");
        CarInsuranceDetails result = vehicleResponse.getBody();
        assertNotNull(result, "Response body should not be null");
        assertEquals(registrationNumber, result.getRegistrationNumber());
        assertNotNull(result.getMake(), "Make should not be null");
        assertNotNull(result.getModel(), "Model should not be null");
        assertTrue(result.getYear() > 1900 && result.getYear() <= java.time.Year.now().getValue() + 1,
            "Year should be a valid vehicle year");
        assertNotNull(result.getColor(), "Color should not be null");
    }

    @Test
    void getVehicleInfo_shouldReturn404_whenRegistrationDoesNotExist() {
        // Given - Using a non-existent registration number
        String nonExistentRegistration = "NONEXISTENT";
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(API_PATH)
                .path(nonExistentRegistration)
                .toUriString();

        // When/Then
        try {
            restTemplate.getForEntity(url, CarInsuranceDetails.class);
            fail("Expected RestClientResponseException was not thrown");
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound ex) {
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        }
    }

    @Test
    void getVehicleInfo_shouldReturn400_whenRegistrationIsInvalid() {
        // Given
        String invalidRegistration = "";
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path(API_PATH)
                .path(invalidRegistration)
                .toUriString();

        // When/Then
        try {
            restTemplate.getForEntity(url, CarInsuranceDetails.class);
            fail("Expected RestClientResponseException was not thrown");
        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest ex) {
            assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        }
    }

    @Test
    void vehicleServiceHealthCheck() {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            BASE_URL + "/actuator/health", 
            String.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
            "Vehicle service health check should return 200 OK");
    }
}
