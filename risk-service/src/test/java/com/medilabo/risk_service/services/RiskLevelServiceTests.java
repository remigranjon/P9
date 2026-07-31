package com.medilabo.risk_service.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.medilabo.risk_service.models.Note;
import com.medilabo.risk_service.models.Patient;
import com.medilabo.risk_service.utils.Keywords;

public class RiskLevelServiceTests {

    private RiskLevelService riskLevelService;

    @BeforeEach
    void setUp() {
        riskLevelService = new RiskLevelService();
    }

    @Test
    void calculateAge_shouldReturnExpectedAge() throws Exception {
        Patient patient = Patient.builder()
                .dateOfBirth(LocalDate.now().minusYears(40).format(DateTimeFormatter.ISO_DATE))
                .build();

        int age = invokeCalculateAge(patient);

        assertEquals(40, age);
    }

    @Test
    void countTriggerTerms_shouldCountCaseInsensitiveKeywords() throws Exception {
        Note[] notes = new Note[] {
                Note.builder().content("Le patient présente un POIDS élevé et est fumeur").build(),
                Note.builder().content("Aucun autre terme déclencheur ici").build()
        };

        int count = invokeCountTriggerTerms(notes);

        assertEquals(2, count);
    }

    @Test
    void countTriggerTerms_shouldReturnZeroWhenNoKeywordFound() throws Exception {
        Note[] notes = new Note[] {
                Note.builder().content("Texte neutre sans mot-clé").build(),
                Note.builder().content("Encore un texte sans indicateur").build()
        };

        int count = invokeCountTriggerTerms(notes);

        assertEquals(0, count);
    }

    @Test
    void calculateRiskLevel_shouldReturnEarlyOnset_forMaleUnder30WithAtLeast5Triggers() throws Exception {
        Patient patient = buildPatientWithAgeAndGender(29, "M");
        Note[] notes = buildNotesWithTriggerCount(5);

        String risk = invokeCalculateRiskLevel(patient, notes);

        assertEquals("Early onset", risk);
    }

    @Test
    void calculateRiskLevel_shouldReturnInDanger_forFemaleUnder30WithAtLeast4Triggers() throws Exception {
        Patient patient = buildPatientWithAgeAndGender(28, "F");
        Note[] notes = buildNotesWithTriggerCount(4);

        String risk = invokeCalculateRiskLevel(patient, notes);

        assertEquals("In danger", risk);
    }

    @Test
    void calculateRiskLevel_shouldReturnBorderline_forPatient30OrMoreWithAtLeast2Triggers() throws Exception {
        Patient patient = buildPatientWithAgeAndGender(45, "M");
        Note[] notes = buildNotesWithTriggerCount(2);

        String risk = invokeCalculateRiskLevel(patient, notes);

        assertEquals("Borderline", risk);
    }

    @Test
    void calculateRiskLevel_shouldReturnNone_whenThresholdsAreNotReached() throws Exception {
        Patient patient = buildPatientWithAgeAndGender(26, "M");
        Note[] notes = buildNotesWithTriggerCount(2);

        String risk = invokeCalculateRiskLevel(patient, notes);

        assertEquals("None", risk);
    }

    private Patient buildPatientWithAgeAndGender(int age, String gender) {
        return Patient.builder()
                .dateOfBirth(LocalDate.now().minusYears(age).format(DateTimeFormatter.ISO_DATE))
                .gender(gender)
                .build();
    }

    private Note[] buildNotesWithTriggerCount(int triggerCount) {
        Keywords[] keywords = Keywords.values();
        Note[] notes = new Note[triggerCount];

        for (int i = 0; i < triggerCount; i++) {
            String keyword = keywords[i % keywords.length].getValue();
            notes[i] = Note.builder()
                    .content("Observation clinique : " + keyword)
                    .build();
        }
        return notes;
    }

    private int invokeCalculateAge(Patient patient) throws Exception {
        Method method = RiskLevelService.class.getDeclaredMethod("calculateAge", Patient.class);
        method.setAccessible(true);
        return (int) method.invoke(riskLevelService, patient);
    }

    private int invokeCountTriggerTerms(Note[] notes) throws Exception {
        Method method = RiskLevelService.class.getDeclaredMethod("countTriggerTerms", Note[].class);
        method.setAccessible(true);
        return (int) method.invoke(riskLevelService, (Object) notes);
    }

    private String invokeCalculateRiskLevel(Patient patient, Note[] notes) throws Exception {
        Method method = RiskLevelService.class.getDeclaredMethod("calculateRiskLevel", Patient.class, Note[].class);
        method.setAccessible(true);
        return (String) method.invoke(riskLevelService, patient, notes);
    }
}