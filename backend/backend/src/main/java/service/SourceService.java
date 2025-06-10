package service;

import entity.Source;
import org.springframework.stereotype.Service;
import repository.SourceRepository;


import java.util.List;

@Service
public class SourceService {

    private final SourceRepository sourceRepository;

    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public List<Source> findAll() {
        return sourceRepository.findAll();
    }

    public Source findById(Long id) {
        return sourceRepository.findById(id).orElse(null);
    }

    public Source save(Source customer) {
        return sourceRepository.save(customer);
    }
    public void delete(Long id) {
        sourceRepository.deleteById(id);
    }
}
