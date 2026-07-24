package com.medilabo.risk_service.services;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.medilabo.risk_service.models.Note;
import com.medilabo.risk_service.models.Patient;
import com.medilabo.risk_service.utils.Keywords;

@Service
public class RiskLevelService {

    public String getRiskLevel(Long patientId) {
        String patientServiceUrl = System.getenv("PATIENT_SERVICE_URL") + "/patients/" + patientId;
        String notesServiceUrl = System.getenv("NOTES_SERVICE_URL") + "/notes/patient/" + patientId;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Patient> response = restTemplate.getForEntity(patientServiceUrl, Patient.class);
            System.out.println("Patient service response body: " + response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                Patient patientFound = response.getBody() ;
                if (patientFound == null) {
                    throw new RuntimeException("Patient not found");
                }
                System.out.println("Patient found: " + patientFound);
                ResponseEntity<Note[]> notesResponse = restTemplate.getForEntity(notesServiceUrl, Note[].class);
                System.out.println("Notes service response: " + notesResponse);
                if (notesResponse.getStatusCode().is2xxSuccessful()) {
                    Note[] notes = notesResponse.getBody();
                    if (notes == null) {
                        throw new RuntimeException("Notes not found");
                    }
                    return calculateRiskLevel(patientFound, notes);
                } else {
                    throw new RuntimeException("Failed to fetch notes. Status code: " + notesResponse.getStatusCode());
                }
            } else {
                throw new RuntimeException("Failed to fetch patient data. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching patient data: " + e.getMessage());
        }
    }

    private String calculateRiskLevel(Patient patient, Note[] notes) {
        if ((patient.getGender().equals("M") && calculateAge(patient) < 30 && countTriggerTerms(notes) >= 5) ||
            (patient.getGender().equals("F") && calculateAge(patient) < 30 && countTriggerTerms(notes) >= 7) ||
            (calculateAge(patient) >= 30 && countTriggerTerms(notes) >= 8)){
            return "Early onset";
        } else if ((patient.getGender().equals("M") && calculateAge(patient) < 30 && countTriggerTerms(notes) >= 3) ||
            (patient.getGender().equals("F") && calculateAge(patient) < 30 && countTriggerTerms(notes) >= 4) ||
            (calculateAge(patient) >= 30 && countTriggerTerms(notes) >= 6)){
            return "In danger";
        } else if ((calculateAge(patient) >= 30 && countTriggerTerms(notes) >= 2)){
            return "Borderline";
        } else {
            return "None";
        }
    }

    private int calculateAge (Patient patient) {
        String[] dateParts = patient.getDateOfBirth().split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();

        return Period.between(birthDate, currentDate).getYears();
    }

    private int countTriggerTerms(Note[] notes) {
        int count = 0;
        for (Note note : notes) {
            for (Keywords keyword : Keywords.values()) {
                if (note.getContent().toLowerCase().contains(keyword.getValue().toLowerCase())) {
                    count++;
                }
            }
        }
        return count;
    }

}
