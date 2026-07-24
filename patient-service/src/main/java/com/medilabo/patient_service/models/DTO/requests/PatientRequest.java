package com.medilabo.patient_service.models.DTO.requests;

import com.medilabo.patient_service.models.entities.Patient;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRequest {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    @NotNull(message = "Date of birth is required")
    private String dateOfBirth;
    @NotNull(message = "Gender is required")
    private String gender;
    private String street;
    private String city;
    private String zipCode;
    private String phoneNumber;

    public Patient toPatient() {
        return Patient.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .dateOfBirth(java.sql.Date.valueOf(this.dateOfBirth))
                .gender(this.gender)
                .street(this.street)
                .city(this.city)
                .zipCode(this.zipCode)
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
