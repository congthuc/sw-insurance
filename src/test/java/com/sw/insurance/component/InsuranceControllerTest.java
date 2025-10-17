package com.sw.insurance.component;

import com.sw.insurance.TestDataFactory;
import com.sw.insurance.controller.InsuranceController;
import com.sw.insurance.dto.InsuranceResponse;
import com.sw.insurance.exception.GlobalExceptionHandler;
import com.sw.insurance.service.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InsuranceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InsuranceService insuranceService;

    @InjectMocks
    private InsuranceController insuranceController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(insuranceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getInsurancesByPersonalId_shouldReturnListOfInsurances() throws Exception {
        // Given
        String personalId = "ID12345678";
        InsuranceResponse carInsurance = TestDataFactory.createCarInsuranceResponse();
        InsuranceResponse petInsurance = TestDataFactory.createPetInsuranceResponse();
        
        when(insuranceService.getInsurancesByPersonalId(personalId))
            .thenReturn(List.of(carInsurance, petInsurance));

        // When & Then
        mockMvc.perform(get("/api/v1/insurances/customer/{personalId}", personalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productCode", is("CAR")))
                .andExpect(jsonPath("$[0].productName", is("Car Insurance")))
                .andExpect(jsonPath("$[0].monthlyPrice", is(30.00)))
                .andExpect(jsonPath("$[0].details.make", is("Toyota")))
                .andExpect(jsonPath("$[1].productCode", is("PET")))
                .andExpect(jsonPath("$[1].productName", is("Pet Insurance")))
                .andExpect(jsonPath("$[1].monthlyPrice", is(10.00)))
                .andExpect(jsonPath("$[1].details.petName", is("Max")));

        verify(insuranceService).getInsurancesByPersonalId(personalId);
        verifyNoMoreInteractions(insuranceService);
    }

    @Test
    void getInsurancesByPersonalId_shouldReturnEmptyList_whenNoInsurancesFound() throws Exception {
        // Given
        String personalId = "ID12345678";
        when(insuranceService.getInsurancesByPersonalId(personalId)).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get("/api/v1/insurances/customer/{personalId}", personalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(insuranceService).getInsurancesByPersonalId(personalId);
        verifyNoMoreInteractions(insuranceService);
    }

    @Test
    void getInsurancesByPersonalId_shouldReturn404_whenPersonNotFound() throws Exception {
        // Given
        String personalId = "NON_EXISTENT";
        when(insuranceService.getInsurancesByPersonalId(personalId))
            .thenThrow(new jakarta.persistence.EntityNotFoundException("Person not found with personal ID: " + personalId));

        // When & Then
        mockMvc.perform(get("/api/v1/insurances/customer/{personalId}", personalId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Person not found with personal ID: " + personalId)))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(insuranceService).getInsurancesByPersonalId(personalId);
        verifyNoMoreInteractions(insuranceService);
    }
}
