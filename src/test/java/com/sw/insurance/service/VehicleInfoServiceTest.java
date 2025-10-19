package com.sw.insurance.service;

import com.sw.insurance.TestDataFactory;
import com.sw.insurance.dto.CarInsuranceDetails;
import com.sw.insurance.dto.VehicleInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleInfoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VehicleInfoService vehicleInfoService;

    private static final String TEST_REGISTRATION = "ABC123";
    private static final String BASE_URL = "http://vehicle-service";
    private static final String API_PATH = "/api/v1/vehicles/";

    @BeforeEach
    void setUp() {
        // Set up test properties
        ReflectionTestUtils.setField(vehicleInfoService, "vehicleServiceUrl", BASE_URL);
        ReflectionTestUtils.setField(vehicleInfoService, "vehicleServicePath", API_PATH);
    }

    @Test
    void getVehicleInfo_shouldReturnVehicleDetails_whenRegistrationExists() {
        // Given
        VehicleInfo mockVehicleInfo = TestDataFactory.createVehicleInfo();
        String expectedUrl = BASE_URL + API_PATH + TEST_REGISTRATION;
        
        when(restTemplate.getForObject(eq(expectedUrl), eq(VehicleInfo.class)))
            .thenReturn(mockVehicleInfo);

        // When
        CarInsuranceDetails result = vehicleInfoService.getVehicleInfo(TEST_REGISTRATION);

        // Then
        assertNotNull(result);
        assertEquals(mockVehicleInfo.getRegistrationNumber(), result.getRegistrationNumber());
        assertEquals(mockVehicleInfo.getMake(), result.getMake());
        assertEquals(mockVehicleInfo.getModel(), result.getModel());
        assertEquals(mockVehicleInfo.getYear(), result.getYear());
        assertEquals(mockVehicleInfo.getColor(), result.getColor());
        
        verify(restTemplate).getForObject(eq(expectedUrl), eq(VehicleInfo.class));
    }

    @Test
    void getVehicleInfo_shouldReturnNull_whenRegistrationIsNull() {
        // When
        CarInsuranceDetails result = vehicleInfoService.getVehicleInfo(null);

        // Then
        assertNull(result);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void getVehicleInfo_shouldReturnNull_whenRegistrationIsEmpty() {
        // When
        CarInsuranceDetails result = vehicleInfoService.getVehicleInfo("");

        // Then
        assertNull(result);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void getVehicleInfo_shouldReturnNull_whenServiceReturnsNull() {
        // Given
        String expectedUrl = BASE_URL + API_PATH + TEST_REGISTRATION;
        when(restTemplate.getForObject(eq(expectedUrl), eq(VehicleInfo.class)))
            .thenReturn(null);

        // When
        CarInsuranceDetails result = vehicleInfoService.getVehicleInfo(TEST_REGISTRATION);

        // Then
        assertNull(result);
        verify(restTemplate).getForObject(eq(expectedUrl), eq(VehicleInfo.class));
    }

    @Test
    void getVehicleInfo_shouldReturnNull_whenServiceThrowsException() {
        // Given
        String expectedUrl = BASE_URL + API_PATH + TEST_REGISTRATION;
        when(restTemplate.getForObject(eq(expectedUrl), eq(VehicleInfo.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // When
        CarInsuranceDetails result = vehicleInfoService.getVehicleInfo(TEST_REGISTRATION);

        // Then
        assertNull(result);
        verify(restTemplate).getForObject(eq(expectedUrl), eq(VehicleInfo.class));
    }
}
