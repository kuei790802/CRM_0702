package controller;

import entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService service;

    // Read All
    private List<Event> getAll() {
        return service.findAll();
    }

    // Read By id
    @GetMapping("/{id}")
    public Event getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Event create(@RequestBody Event event) {
        return service.save(event);
    }

    // Update
    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @RequestBody Event event) {
        event.setId(id);
        return service.save(event);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
