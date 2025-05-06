package org.deltacore.delta.service;

import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.model.Activity;
import org.deltacore.delta.repositorie.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivitiesSectionService {
    private static final int DEFAULT_LIMIT = 20;
    private final ActivityDAO activityDAO;

    @Autowired
    public ActivitiesSectionService(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    public void saveActivity(ActivityDTO activity) {
        // Usar MapStruct depois pra conversão de DTO pra Entity
        Activity activityForSave = Activity.builder()
                .title(activity.title())
                .description(activity.description())
                .activityType(activity.activityType())
                .imageUrl(activity.imageUrl())
                .recommendedLevel(activity.recommendedLevel())
                .maxScore(activity.maxScore())
                .build();

        activityDAO.save(activityForSave);
    }

    public List<Activity> getLimitedActivities(String search) { // Busca atividades com 1 a n strings de quaisquer tamanhos
        if (search == null || search.trim().isEmpty()) { // Eu espero que isso receba uma string vazia, ao invés de null
            Optional<List<Activity>> activities = Optional
                    .ofNullable((List<Activity>) activityDAO
                            .findAllActivities(DEFAULT_LIMIT)); // Limite de 20 atividades por busca, resolvo isso depois
            return activities.orElse(Collections.emptyList());
        }
        return searchDetailed(search);
    }

    protected List<Activity> searchDetailed(String search) {
        final String[] words = search.split("\\s+");
        final Set<String> setWords = new HashSet<>(Arrays.asList(words));

        Set<Activity> activities = new HashSet<>();

        setWords.forEach(word -> { // Ele vai procurar uma lista de atividades que contenham cada palavra
            Optional<List<Activity>> found = Optional.ofNullable((List<Activity>) activityDAO.findActivitiesByTitle(word));
            found.ifPresent(activities::addAll);
        });
        // Tem que implementar paginação aqui
        return activities.stream().toList();
    }


}
