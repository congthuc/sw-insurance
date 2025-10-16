package com.sw.insurance.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CarInsuranceDetails.class, name = "CAR"),
    @JsonSubTypes.Type(value = PetInsuranceDetails.class, name = "PET"),
    @JsonSubTypes.Type(value = HealthInsuranceDetails.class, name = "HEALTH")
})
public abstract class InsuranceDetails {
    // Base class for all insurance details
}
