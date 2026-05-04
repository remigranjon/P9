package com.medilabo.patient_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.patient_service.models.DTO.requests.PatientRequest;
import com.medilabo.patient_service.models.DTO.responses.PatientResponse;
import com.medilabo.patient_service.services.PatientService;

import jakarta.validation.Valid;

@RestController
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getPatients() {
        List<PatientResponse> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PostMapping("/patients")
    public ResponseEntity<PatientResponse> savePatient(@Valid @RequestBody PatientRequest patient) {
        PatientResponse savedPatient = patientService.savePatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable String id,
            @Valid @RequestBody PatientRequest patient) {
       try {
           PatientResponse updatedPatient = patientService.updatePatient(id, patient);
           return ResponseEntity.ok(updatedPatient);
       } catch (Exception e) {
           return ResponseEntity.notFound().build();
       }
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable String id) {
        try {
            PatientResponse patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
