package com.medilabo.patient_service.models.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.medilabo.patient_service.enums.PatientGender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    private String id;
    @NotEmpty(message = "First name is required")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotEmpty(message = "Last name is required")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @NotEmpty(message = "Date of birth is required")
    @NotBlank(message = "Date of birth cannot be blank")
    private Date dateOfBirth;
    @NotEmpty(message = "Gender is required")
    private PatientGender gender;
    private Address address;
    private String phoneNumber;
}


