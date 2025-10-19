package com.sw.insurance;

import com.sw.insurance.dto.CarInsuranceDetails;
import com.sw.insurance.dto.HealthInsuranceDetails;
import com.sw.insurance.dto.InsuranceResponse;
import com.sw.insurance.dto.PetInsuranceDetails;
import com.sw.insurance.model.Person;
import com.sw.insurance.model.Pet;
import com.sw.insurance.model.Policy;
import com.sw.insurance.model.PolicyDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class TestDataFactory {

    public static Person createPerson() {
        Person person = new Person();
        person.setId(1L);
        person.setPersonalId("ID12345678");
        person.setName("John Doe");
        person.setEmail("john.doe@example.com");
        person.setPhone("+1234567890");
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        return person;
    }

    public static Pet createPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Max");
        pet.setSpecies("Dog");
        pet.setBreed("Golden Retriever");
        pet.setBirthDate(LocalDate.of(2020, 5, 15));
        pet.setMicrochipNumber("A1234567890");
        return pet;
    }

    public static Policy createCarPolicy() {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setProductCode("CAR");
        policy.setMonthlyPrice(new BigDecimal("30.00"));
        policy.setStatus("ACTIVE");
        policy.setPersonId(1L);
        return policy;
    }

    public static Policy createPetPolicy() {
        Policy policy = new Policy();
        policy.setId(2L);
        policy.setProductCode("PET");
        policy.setMonthlyPrice(new BigDecimal("10.00"));
        policy.setStatus("ACTIVE");
        policy.setPersonId(1L);
        return policy;
    }

    public static Policy createHealthPolicy() {
        Policy policy = new Policy();
        policy.setId(3L);
        policy.setProductCode("HEALTH");
        policy.setMonthlyPrice(new BigDecimal("20.00"));
        policy.setStatus("ACTIVE");
        policy.setPersonId(1L);
        return policy;
    }

    public static PolicyDetails createCarPolicyDetails() {
        PolicyDetails details = new PolicyDetails();
        details.setId(1L);
        details.setProductCode("CAR");
        details.setVehicleRegistration("ABC123");
        return details;
    }

    public static PolicyDetails createPetPolicyDetails() {
        PolicyDetails details = new PolicyDetails();
        details.setId(2L);
        details.setProductCode("PET");
        details.setPet(createPet());
        return details;
    }

    public static PolicyDetails createHealthPolicyDetails() {
        PolicyDetails details = new PolicyDetails();
        details.setId(3L);
        details.setProductCode("HEALTH");
        details.setHealthInfo(Map.of(
            "policyNumber", "HLTH123456",
            "coverageType", "FAMILY",
            "primaryHolder", "John Doe",
            "dependents", List.of("Jane Doe", "Jimmy Doe")
        ));
        return details;
    }

    public static com.sw.insurance.dto.VehicleInfo createVehicleInfo() {
        com.sw.insurance.dto.VehicleInfo vehicleInfo = new com.sw.insurance.dto.VehicleInfo();
        vehicleInfo.setRegistrationNumber("ABC123");
        vehicleInfo.setMake("Toyota");
        vehicleInfo.setModel("Camry");
        vehicleInfo.setYear(2020);
        vehicleInfo.setColor("Silver");
        return vehicleInfo;
    }
    
    public static CarInsuranceDetails createCarInsuranceDetails(com.sw.insurance.dto.VehicleInfo vehicleInfo) {
        if (vehicleInfo == null) {
            vehicleInfo = createVehicleInfo();
        }
        CarInsuranceDetails details = new CarInsuranceDetails();
        details.setRegistrationNumber(vehicleInfo.getRegistrationNumber());
        details.setMake(vehicleInfo.getMake());
        details.setModel(vehicleInfo.getModel());
        details.setYear(vehicleInfo.getYear());
        details.setColor(vehicleInfo.getColor());
        return details;
    }

    public static InsuranceResponse createCarInsuranceResponse() {
        com.sw.insurance.dto.VehicleInfo vehicleInfo = createVehicleInfo();
        CarInsuranceDetails details = new CarInsuranceDetails();
        details.setRegistrationNumber(vehicleInfo.getRegistrationNumber());
        details.setMake(vehicleInfo.getMake());
        details.setModel(vehicleInfo.getModel());
        details.setYear(vehicleInfo.getYear());
        details.setColor(vehicleInfo.getColor());
        
        return new InsuranceResponse(
            "CAR",
            "Car Insurance",
            new BigDecimal("30.00"),
            details
        );
    }

    public static InsuranceResponse createPetInsuranceResponse() {
        PetInsuranceDetails details = new PetInsuranceDetails();
        Pet pet = createPet();
        details.setPetName(pet.getName());
        details.setSpecies(pet.getSpecies());
        details.setBreed(pet.getBreed());
        
        return new InsuranceResponse(
            "PET",
            "Pet Insurance",
            new BigDecimal("10.00"),
            details
        );
    }

    public static InsuranceResponse createHealthInsuranceResponse() {
        HealthInsuranceDetails details = new HealthInsuranceDetails();
        details.setPolicyNumber("HLTH123456");
        details.setCoverageType("FAMILY");
        details.setPrimaryHolder("John Doe");
        details.setDependents(List.of("Jane Doe", "Jimmy Doe"));
        
        return new InsuranceResponse(
            "HEALTH",
            "Health Insurance",
            new BigDecimal("20.00"),
            details
        );
    }
}
