package service;

import entity.Opportunity;
import org.springframework.stereotype.Service;
import repository.OpportunityRepository;

import java.util.List;

@Service
public class OpportunityService {

    private final OpportunityRepository opportunityRepository;

    public OpportunityService(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }

    public List<Opportunity> findAll() {
        return opportunityRepository.findAll();
    }

    public Opportunity findById(Long id) {
        return opportunityRepository.findById(id).orElse(null);
    }

    public Opportunity save(Opportunity opportunity) {
        return opportunityRepository.save(opportunity);
    }

    public void delete(Long id) {
        opportunityRepository.deleteById(id);
    }
}
