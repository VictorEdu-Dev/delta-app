package org.deltacore.delta.service;

import jakarta.servlet.Filter;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.dto.ActivityFilterDTO;
import org.deltacore.delta.dto.ActivityMapper;
import org.deltacore.delta.dto.ActivityTsdtDTO;
import org.deltacore.delta.exception.ConflictException;
import org.deltacore.delta.model.Activity;
import org.deltacore.delta.model.ActivityStatus;
import org.deltacore.delta.model.ActivityType;
import org.deltacore.delta.repositorie.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ActivitiesSectionService {
    private static final int DEFAULT_LIMIT = 20;

    private final ActivityMapper activityMapper;
    private final ActivityDAO activityDAO;
    private final PagedResourcesAssembler<Activity> pagedResourcesAssembler;
    private final Filter filter;

    @Autowired
    public ActivitiesSectionService(ActivityDAO activityDAO, ActivityMapper activityMapper, PagedResourcesAssembler<Activity> pagedResourcesAssembler, @Qualifier("requestContextFilter") Filter filter) {
        this.activityMapper = activityMapper;
        this.activityDAO = activityDAO;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.filter = filter;
    }

    public ActivityDTO saveActivity(ActivityDTO activity) {
        Optional<String> actDouble = activityDAO.findActByTitle(activity.title());
        if (actDouble.isPresent()) throw new ConflictException(activity.title() + " already exists!");

        Activity activityForSave = activityMapper.toEntity(activity);
        setDefaultValues(activityForSave);

        return activityMapper.toDTO(activityDAO.save(activityForSave));
    }

    private void setDefaultValues(Activity activityForSave) {
        if (activityForSave.getStatus() == null) {
            activityForSave.setStatus(ActivityStatus.PENDING);
        }
        if (activityForSave.getDeadline() == null) {
            activityForSave.setDeadline(LocalDateTime.now().plusDays(7));
        }
    }

    public PagedModel<EntityModel<ActivityDTO>> getActivitiesFiltered(Pageable pageable, ActivityFilterDTO filters) {
        Page<Activity> activities = activityDAO.findActivitiesByFilters(
                filters.status(),
                filters.activityType(),
                filters.startDate() != null ? filters.startDate().atStartOfDay()
                        : LocalDate.now().atStartOfDay(),
                filters.endDate() != null ? filters.endDate().atTime(LocalTime.MAX)
                        : LocalDateTime.of(3000, 12, 31, 23, 59),
                pageable);

        if (activities.isEmpty()) return PagedModel.empty();

        return pagedResourcesAssembler
                .toModel(activities, activity -> EntityModel.of(activityMapper.toDTO(activity)));
    }

    public List<ActivityDTO> getLimitedActivities(String search) {
        if (search == null || search.trim().isEmpty()) {
            List<Activity> activities = Optional
                    .ofNullable((List<Activity>) activityDAO.findAllActivities(DEFAULT_LIMIT))
                    .orElse(Collections.emptyList());

            return activities
                    .isEmpty() ? Collections.emptyList() : activities
                    .stream()
                    .map(activityMapper::toDTO)
                    .toList();
        }

        List<ActivityDTO> activities = searchDetailed(search);
        if (activities.isEmpty()) return getLimitedActivities("");
        return activities;
    }

    protected List<ActivityDTO> searchDetailed(String search) {
        final String[] words = search.split("\\s+");
        final Set<String> setWords = new HashSet<>(Arrays.asList(words));

        Set<Activity> activities = new HashSet<>();

        setWords.forEach(word -> {
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
