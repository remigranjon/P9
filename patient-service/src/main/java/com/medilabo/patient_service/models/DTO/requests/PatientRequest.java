package com.medilabo.patient_service.models.DTO.requests;

import com.medilabo.patient_service.enums.PatientGender;
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
    private AddressRequest address;
    private String phoneNumber;

    public Patient toPatient() {
        return Patient.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .dateOfBirth(java.sql.Date.valueOf(this.dateOfBirth))
                .gender(PatientGender.valueOf(this.gender))
                .address(this.address.toAddress())
                .phoneNumber(this.phoneNumber)
                .build();
    }
}
