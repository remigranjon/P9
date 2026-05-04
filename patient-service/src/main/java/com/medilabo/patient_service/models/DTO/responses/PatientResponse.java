package com.medilabo.patient_service.models.DTO.responses;

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
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private AddressResponse address;
    private String phoneNumber;

    public PatientResponse(Patient patient) {
        this.id = patient.getId();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.dateOfBirth = patient.getDateOfBirth().toString();
        this.gender = patient.getGender().name();
        this.address = new AddressResponse(patient.getAddress().getStreet(), patient.getAddress().getCity(), patient.getAddress().getZipCode());
        this.phoneNumber = patient.getPhoneNumber();
    
    }
}
