package com.medilabo.patient_service.repositories.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.medilabo.patient_service.models.entities.Patient;

public interface PatientRepository extends MongoRepository<Patient, String> {

}
