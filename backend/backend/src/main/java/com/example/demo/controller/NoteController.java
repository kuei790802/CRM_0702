package com.example.demo.controller;



import com.example.demo.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.NoteService;

import java.util.List;


@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService service;

    // Read All
    @GetMapping
    public List<Note> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Note getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping("/{id}")
    public Note create(@RequestBody Note note) {
        return service.save(note);
    }

    // Update
    @PutMapping("/{id}")
    public Note update(@PathVariable Long id, @RequestBody Note note) {
        note.setId(id);
        return service.save(note);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
