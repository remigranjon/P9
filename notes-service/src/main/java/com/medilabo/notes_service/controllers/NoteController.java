package com.medilabo.notes_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medilabo.notes_service.models.entities.Note;
import com.medilabo.notes_service.services.NoteService;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Note>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(noteService.getByPatientId(patientId));
    }

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<Note> create(@PathVariable Long patientId, @RequestBody String content) {
        return ResponseEntity.ok(noteService.create(patientId, content));
    }

    @PostMapping("/note/{noteId}")
    public ResponseEntity<Note> update(@PathVariable String noteId, @RequestBody String content) {
        return ResponseEntity.ok(noteService.update(noteId, content));
    }

    @DeleteMapping("/note/{noteId}")
    public ResponseEntity<Void> delete(@PathVariable String noteId) {
        noteService.delete(noteId);
        return ResponseEntity.noContent().build();
    }


}
