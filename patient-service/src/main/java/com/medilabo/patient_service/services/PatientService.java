package com.medilabo.patient_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medilabo.patient_service.models.DTO.requests.PatientRequest;
import com.medilabo.patient_service.models.DTO.responses.PatientResponse;
import com.medilabo.patient_service.models.entities.Patient;
import com.medilabo.patient_service.repositories.interfaces.PatientRepository;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    public void deletePatientById(String id) {
        patientRepository.deleteById(id);
    }

    public PatientResponse getPatientById(String id) throws RuntimeException {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        return new PatientResponse(patient);
    }

    public PatientResponse savePatient(PatientRequest patient) {
        Patient savedPatient = patientRepository.save(patient.toPatient());
        return new PatientResponse(savedPatient);
    }

    public PatientResponse updatePatient(String id, PatientRequest patient) throws RuntimeException {
        Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
        Patient updatedPatient = patient.toPatient();
        updatedPatient.setId(existingPatient.getId());
        updatedPatient = patientRepository.save(updatedPatient);
        return new PatientResponse(updatedPatient);
    }

    public List<PatientResponse> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientResponse::new).collect(Collectors.toList());
    }
}
