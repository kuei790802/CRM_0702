package service;

import entity.Activity;
import org.springframework.stereotype.Service;
import repository.ActivityRepository;


import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Activity findById(Long id) {
        return activityRepository.findById(id).orElse(null);
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }
    public void delete(Long id) {
        activityRepository.deleteById(id); }

}

