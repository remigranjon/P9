package com.medilabo.patient_service.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medilabo.patient_service.models.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
