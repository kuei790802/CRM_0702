package service;

import entity.Stage;
import org.springframework.stereotype.Service;
import repository.StageRepository;

import java.util.List;

@Service
public class StageService {

    private final StageRepository stageRepository;

    public StageService(StageRepository stageRepository) {
        this.stageRepository = stageRepository;
    }

    public List<Stage> findAll() {
        return stageRepository.findAll();
    }

    public Stage findById(Long id) {
        return stageRepository.findById(id).orElse(null);
    }

    public Stage save(Stage customer) {
        return stageRepository.save(customer);
    }
    public void delete(Long id) {
        stageRepository.deleteById(id);
    }
}
