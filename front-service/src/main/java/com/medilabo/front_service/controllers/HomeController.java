package com.medilabo.front_service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.medilabo.front_service.models.NoteResponse;
import com.medilabo.front_service.models.PatientRequest;
import com.medilabo.front_service.models.PatientResponse;

import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
            @RequestParam String password,
            Model model) {
        // Préparer la requête pour user-service
        String userServiceUrl = System.getenv("USER_SERVICE_URL") + "/auth/register";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(userServiceUrl, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Inscription réussie !");
                return "register";
            } else {
                model.addAttribute("error", "Erreur lors de l'inscription.");
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'inscription : " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/patients")
    public String showPatients(Model model) {
        String patientServiceUrl = System.getenv("PATIENT_SERVICE_URL") + "/patients";
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<PatientResponse[]> response = restTemplate.getForEntity(patientServiceUrl,
                    PatientResponse[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("patients", response.getBody());
            } else {
                model.addAttribute("error", "Erreur lors de la récupération des patients.");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des patients : " + e.getMessage());
        }
        return "patients";
    }

    @GetMapping("/patients/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new PatientRequest());
        return "add_patient";
    }

    @PostMapping("/patients/add")
    public String addPatient(@ModelAttribute PatientRequest patient, Model model) {
        model.addAttribute("patient", patient);
        String patientServiceUrl = System.getenv("PATIENT_SERVICE_URL") + "/patients";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PatientRequest> request = new HttpEntity<>(patient, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(patientServiceUrl, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("error", "Erreur lors de l'ajout du patient.");
                return "add_patient";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout du patient : " + e.getMessage());
            return "add_patient";
        }
        return "redirect:/patients";
    }

    @GetMapping("/patients/edit/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model) {
        String patientServiceUrl = System.getenv("PATIENT_SERVICE_URL") + "/patients/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<PatientResponse> response = restTemplate.getForEntity(patientServiceUrl,
                    PatientResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("patient", response.getBody());
                return "edit_patient";
            } else {
                model.addAttribute("error", "Erreur lors de la récupération du patient.");
                return "redirect:/patients";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération du patient : " + e.getMessage());
            return "redirect:/patients";
        }
    }

    @PostMapping("/patients/edit/{id}")
    public String editPatient(@PathVariable Long id, @ModelAttribute PatientRequest patient, Model model) {
        model.addAttribute("patient", patient);
        String patientServiceUrl = System.getenv("PATIENT_SERVICE_URL") + "/patients/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PatientRequest> request = new HttpEntity<>(patient, headers);
        try {
            restTemplate.put(patientServiceUrl, request);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la modification du patient : " + e.getMessage());
            return "edit_patient";
        }
        return "redirect:/patients/" + id;
    }

    @GetMapping("/patients/{id}")
    public String getPatient(@PathVariable Long id, Model model) {
        String patientServiceUrl = System.getenv("PATIENT_SERVICE_URL") + "/patients/" + id;
        String noteServiceUrl = System.getenv("NOTES_SERVICE_URL") + "/notes/patient/" + id;
        String riskServiceUrl = System.getenv("RISK_SERVICE_URL") + "/risk-level/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<PatientResponse> response = restTemplate.getForEntity(patientServiceUrl,
                    PatientResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Patient data: " + response.getBody());
                model.addAttribute("patient", response.getBody());
                ResponseEntity<NoteResponse[]> noteResponse = restTemplate.getForEntity(noteServiceUrl,
                        NoteResponse[].class);
                if (noteResponse.getStatusCode().is2xxSuccessful()) {
                    model.addAttribute("notes", noteResponse.getBody());
                } else {
                    model.addAttribute("error", "Erreur lors de la récupération des notes.");
                }
                ResponseEntity<String> riskResponse = restTemplate.getForEntity(riskServiceUrl, String.class);
                if (riskResponse.getStatusCode().is2xxSuccessful()) {
                    model.addAttribute("riskLevel", riskResponse.getBody());
                } else {
                    model.addAttribute("error", "Erreur lors de la récupération du niveau de risque.");
                }
                return "view_patient";
            } else {
                System.out.println("Failed to retrieve patient data. Status code: " + response.getStatusCode());
                model.addAttribute("error", "Erreur lors de la récupération du patient.");
                return "redirect:/patients";
            }
        } catch (Exception e) {
            System.out.println("Exception occurred while retrieving patient data: " + e.getMessage());
            model.addAttribute("error", "Erreur lors de la récupération du patient : " + e.getMessage());
            return "redirect:/patients";
        }
    }

    @GetMapping("/patients/{id}/add-note")
    public String addNote(@PathVariable Long id, Model model) {
        model.addAttribute("patientId", id);
        return "add_note";
    }

    @PostMapping("/patients/{id}/add-note")
    public String saveNote(@PathVariable Long id, @RequestParam String content, Model model) {
        String noteServiceUrl = System.getenv("NOTES_SERVICE_URL") + "/notes/patient/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(content, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(noteServiceUrl, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("error", "Erreur lors de l'ajout de la note.");
                return "add_note";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout de la note : " + e.getMessage());
            return "add_note";
        }
        return "redirect:/patients/" + id;
    }

    @PostMapping("/patients/{patientId}/notes/{noteId}/edit")
    public String editNote(@PathVariable Long patientId, @PathVariable String noteId, @RequestParam String content,
            Model model) {
        String noteServiceUrl = System.getenv("NOTES_SERVICE_URL") + "/notes/note/" + noteId;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(content, headers);

        try {
            restTemplate.postForEntity(noteServiceUrl, request, String.class);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la modification de la note : " + e.getMessage());
        }
        return "redirect:/patients/" + patientId;
    }

    @PostMapping("/patients/{patientId}/notes/{noteId}/delete")
    public String deleteNote(@PathVariable Long patientId, @PathVariable String noteId, Model model) {
        String noteServiceUrl = System.getenv("NOTES_SERVICE_URL") + "/notes/note/" + noteId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.delete(noteServiceUrl);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la suppression de la note : " + e.getMessage());
        }
        return "redirect:/patients/" + patientId;
    }

    @GetMapping("/patients/{patientId}/notes/{noteId}/edit")
    public String editNotePage(@PathVariable Long patientId, @PathVariable String noteId, Model model) {
        String noteServiceUrl = System.getenv("NOTES_SERVICE_URL") + "/notes/patient/" + patientId;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<NoteResponse[]> response = restTemplate.getForEntity(noteServiceUrl, NoteResponse[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                for (NoteResponse note : response.getBody()) {
                    if (noteId.equals(note.getId())) {
                        model.addAttribute("patientId", patientId);
                        model.addAttribute("noteId", noteId);
                        model.addAttribute("content", note.getContent());
                        return "edit_note";
                    }
                }
            }
            model.addAttribute("error", "Note introuvable.");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement de la note : " + e.getMessage());
        }

        model.addAttribute("patientId", patientId);
        model.addAttribute("noteId", noteId);
        model.addAttribute("content", "");
        return "edit_note";
    }
}
