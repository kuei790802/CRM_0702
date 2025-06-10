package controller;

import entity.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.StageService;

import java.util.List;

@RestController
@RequestMapping("/api/stages")
public class StageController {

    @Autowired
    private StageService service;

    // Read All
    @GetMapping
    public List<Stage> getAll() {
        return service.findAll();
    }
    // Read By id
    @GetMapping("/{id}")
    public Stage getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // Create
    @PostMapping
    public Stage create(@RequestBody Stage stage) {
        return service.save(stage);
    }

    // Update
    @PutMapping("/{id}")
    public Stage update(@PathVariable Long id, @RequestBody Stage stage) {
        stage.setId(id);
        return service.save(stage);
    }

    // Delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
