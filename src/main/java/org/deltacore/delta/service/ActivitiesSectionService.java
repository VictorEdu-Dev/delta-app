package org.deltacore.delta.service;

import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.dto.ActivityMapper;
import org.deltacore.delta.dto.ActivityTsdtDTO;
import org.deltacore.delta.model.Activity;
import org.deltacore.delta.repositorie.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivitiesSectionService {
    private static final int DEFAULT_LIMIT = 20;

    private final ActivityMapper activityMapper;
    private final ActivityDAO activityDAO;

    @Autowired
    public ActivitiesSectionService(ActivityDAO activityDAO, ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
        this.activityDAO = activityDAO;
    }

    public ActivityDTO saveActivity(ActivityDTO activity) {
        Activity activityForSave = activityMapper.toEntity(activity);
         return activityMapper.toDTO(activityDAO.save(activityForSave));
    }

    public List<ActivityDTO> getLimitedActivities(String search) {
        // Busca atividades com 1 a n strings de quaisquer tamanhos
        if (search == null || search.trim().isEmpty()) {
            Optional<List<Activity>> activities = Optional
                    .ofNullable((List<Activity>) activityDAO
                            .findAllActivities(DEFAULT_LIMIT));

            return activities.map(activityList -> activityList
                    .stream()
                    .map(activityMapper::toDTO)
                    .toList()).orElse(Collections.emptyList());
        }

        List<ActivityDTO> activities = searchDetailed(search);
        if (activities.isEmpty()) return getLimitedActivities("");
        return activities;
    }

    protected List<ActivityDTO> searchDetailed(String search) {
        final String[] words = search.split("\\s+");
        final Set<String> setWords = new HashSet<>(Arrays.asList(words));

        Set<Activity> activities = new HashSet<>();

        setWords.forEach(word -> { // Ele vai procurar uma lista de atividades que contenham cada palavra
            Optional<List<Activity>> found = Optional.ofNullable((List<Activity>) activityDAO.findActivitiesByTitle(word));
            found.ifPresent(activities::addAll);
        });
        // Tem que implementar paginação aqui
        return  activities
                .stream()
                .map(activityMapper::toDTO)
                .toList();
    }

    public List<ActivityTsdtDTO> getActivitiesWithTitleStatusTypeAndDeadline() {
        List<Activity> act = (List<Activity>) activityDAO.findAllActivities(DEFAULT_LIMIT);
        if (act.isEmpty()) return Collections.emptyList();
        return act
                .stream()
                .map(activityMapper::toTsdtDTO)
                .toList();
    }
}
