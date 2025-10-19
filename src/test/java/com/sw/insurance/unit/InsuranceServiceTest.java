package com.sw.insurance.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sw.insurance.TestConfig;
import com.sw.insurance.TestDataFactory;
import com.sw.insurance.dto.InsuranceResponse;
import com.sw.insurance.exception.PersonNotFoundException;
import com.sw.insurance.model.Person;
import com.sw.insurance.model.Policy;
import com.sw.insurance.model.PolicyDetails;
import com.sw.insurance.repository.PersonRepository;
import com.sw.insurance.repository.PolicyDetailsRepository;
import com.sw.insurance.repository.PolicyRepository;
import com.sw.insurance.service.FeatureFlagService;
import com.sw.insurance.service.InsuranceService;
import com.sw.insurance.service.VehicleInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
class InsuranceServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private PolicyDetailsRepository policyDetailsRepository;

    @Mock
    private VehicleInfoService vehicleInfoService;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private InsuranceService insuranceService;

    private static final String PET_INSURANCE_AVAILABLE = "pet-insurance-available";

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(personRepository, policyRepository, policyDetailsRepository, vehicleInfoService, featureFlagService);
    }

    @Test
    void getInsurancesByPersonalId_shouldThrowException_whenPersonNotFound() {
        // Given
        String personalId = "NON_EXISTENT";
        when(personRepository.findByPersonalId(personalId)).thenReturn(Optional.empty());

        // When & Then
        PersonNotFoundException exception = assertThrows(PersonNotFoundException.class, () -> {
            insuranceService.getInsurancesByPersonalId(personalId);
        });
        
        assertEquals("Person not found with personal ID: " + personalId, exception.getMessage());
        verify(personRepository).findByPersonalId(personalId);
        verifyNoMoreInteractions(policyRepository, policyDetailsRepository);
    }

    @Test
    void getInsurancesByPersonalId_shouldReturnCarInsurance() {
        // Given
        Person person = TestDataFactory.createPerson();
        Policy carPolicy = TestDataFactory.createCarPolicy();
        PolicyDetails carDetails = TestDataFactory.createCarPolicyDetails();
        com.sw.insurance.dto.VehicleInfo vehicleInfo = TestDataFactory.createVehicleInfo();

        when(personRepository.findByPersonalId(person.getPersonalId())).thenReturn(Optional.of(person));
        when(policyRepository.findActivePoliciesByPersonId(person.getId())).thenReturn(List.of(carPolicy));
        when(policyDetailsRepository.findByPolicyId(carPolicy.getId())).thenReturn(Optional.of(carDetails));
        when(vehicleInfoService.getVehicleInfo(carDetails.getVehicleRegistration()))
            .thenReturn(TestDataFactory.createCarInsuranceDetails(vehicleInfo));

        // When
        List<InsuranceResponse> result = insuranceService.getInsurancesByPersonalId(person.getPersonalId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CAR", result.get(0).getProductCode());
        assertEquals("Car Insurance", result.get(0).getProductName());
        assertEquals(0, new BigDecimal("30.00").compareTo(result.get(0).getMonthlyPrice()));
        assertTrue(result.get(0).getDetails() instanceof com.sw.insurance.dto.CarInsuranceDetails);
        
        verify(personRepository).findByPersonalId(person.getPersonalId());
        verify(policyRepository).findActivePoliciesByPersonId(person.getId());
        verify(policyDetailsRepository).findByPolicyId(carPolicy.getId());
        verify(vehicleInfoService).getVehicleInfo(carDetails.getVehicleRegistration());
    }

    @Test
    void getInsurancesByPersonalId_shouldReturnPetInsurance() {
        // Given
        Person person = TestDataFactory.createPerson();
        Policy petPolicy = TestDataFactory.createPetPolicy();
        PolicyDetails petDetails = TestDataFactory.createPetPolicyDetails();

        when(personRepository.findByPersonalId(person.getPersonalId())).thenReturn(Optional.of(person));
        when(policyRepository.findActivePoliciesByPersonId(person.getId())).thenReturn(List.of(petPolicy));
        when(policyDetailsRepository.findByPolicyId(petPolicy.getId())).thenReturn(Optional.of(petDetails));
        // Default behavior for feature flag
        when(featureFlagService.isFeatureEnabled(eq(PET_INSURANCE_AVAILABLE))).thenReturn(true);

        // When
        List<InsuranceResponse> result = insuranceService.getInsurancesByPersonalId(person.getPersonalId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("PET", result.get(0).getProductCode());
        assertEquals("Pet Insurance", result.get(0).getProductName());
        assertEquals(0, new BigDecimal("10.00").compareTo(result.get(0).getMonthlyPrice()));
        assertTrue(result.get(0).getDetails() instanceof com.sw.insurance.dto.PetInsuranceDetails);
        
        verify(personRepository).findByPersonalId(person.getPersonalId());
        verify(policyRepository).findActivePoliciesByPersonId(person.getId());
        verify(policyDetailsRepository).findByPolicyId(petPolicy.getId());
    }

    @Test
    void getInsurancesByPersonalId_shouldReturnHealthInsurance() {
        // Given
        Person person = TestDataFactory.createPerson();
        Policy healthPolicy = TestDataFactory.createHealthPolicy();
        PolicyDetails healthDetails = TestDataFactory.createHealthPolicyDetails();

        when(personRepository.findByPersonalId(person.getPersonalId())).thenReturn(Optional.of(person));
        when(policyRepository.findActivePoliciesByPersonId(person.getId())).thenReturn(List.of(healthPolicy));
        when(policyDetailsRepository.findByPolicyId(healthPolicy.getId())).thenReturn(Optional.of(healthDetails));

        // When
        List<InsuranceResponse> result = insuranceService.getInsurancesByPersonalId(person.getPersonalId());

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("HEALTH", result.get(0).getProductCode());
        assertEquals("Health Insurance", result.get(0).getProductName());
        assertEquals(0, new BigDecimal("20.00").compareTo(result.get(0).getMonthlyPrice()));
        assertTrue(result.get(0).getDetails() instanceof com.sw.insurance.dto.HealthInsuranceDetails);
        
        verify(personRepository).findByPersonalId(person.getPersonalId());
        verify(policyRepository).findActivePoliciesByPersonId(person.getId());
        verify(policyDetailsRepository).findByPolicyId(healthPolicy.getId());
    }
}
