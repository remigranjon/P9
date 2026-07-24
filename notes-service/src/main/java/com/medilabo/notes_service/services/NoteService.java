package com.medilabo.notes_service.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medilabo.notes_service.models.entities.Note;
import com.medilabo.notes_service.repositories.NoteRepository;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getByPatientId(Long patientId) {
        return noteRepository.findByPatientId(patientId);
    }

    public Note create(Long patientId, String content) {
        Note note = new Note();
        note.setPatientId(patientId);
        note.setContent(content);
        return noteRepository.save(note);
    }

    public Note update(String noteId, String content) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + noteId));
        note.setContent(content);
        return noteRepository.save(note);
    }

    public void delete(String noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new IllegalArgumentException("Note not found with id: " + noteId));
        noteRepository.delete(note);
    }

}
