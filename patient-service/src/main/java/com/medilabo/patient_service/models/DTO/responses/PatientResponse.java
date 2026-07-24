package com.medilabo.patient_service.models.DTO.responses;

import java.text.SimpleDateFormat;

import com.medilabo.patient_service.models.entities.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String street;
    private String city;
    private String zipCode;
    private String phoneNumber;

    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(patient.getDateOfBirth());
        this.gender = patient.getGender().getCode();
        this.street = patient.getStreet();
        this.city = patient.getCity();
        this.zipCode = patient.getZipCode();
        this.phoneNumber = patient.getPhoneNumber();
    
    }
}
