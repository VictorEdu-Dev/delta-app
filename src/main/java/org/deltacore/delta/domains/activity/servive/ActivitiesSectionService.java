package org.deltacore.delta.domains.activity.servive;

import org.deltacore.delta.domains.activity.dto.ActivityDTO;
import org.deltacore.delta.domains.activity.dto.ActivityFilterDTO;
import org.deltacore.delta.domains.activity.dto.ActivityMapper;
import org.deltacore.delta.domains.activity.dto.ActivityTsdtDTO;
import org.deltacore.delta.shared.exception.ConflictException;
import org.deltacore.delta.shared.exception.ResourceNotFoundException;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.repository.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ActivitiesSectionService {
    private static final int DEFAULT_LIMIT = 20;
    private static final LocalDateTime DEFAULT_END_DATE = LocalDateTime.of(3000, 12, 31, 23, 59);

    private final ActivityMapper activityMapper;
    private final ActivityDAO activityDAO;
    private final PagedResourcesAssembler<Activity> pagedResourcesAssembler;
    private final MessageSource messageSource;

    @Autowired
    public ActivitiesSectionService(ActivityDAO activityDAO,
                                    ActivityMapper activityMapper,
                                    PagedResourcesAssembler<Activity> pagedResourcesAssembler,
                                    MessageSource messageSource) {
        this.activityMapper = activityMapper;
        this.activityDAO = activityDAO;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.messageSource = messageSource;
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
                        : DEFAULT_END_DATE,
                pageable);

        if (activities.isEmpty()) return PagedModel.empty();

        return pagedResourcesAssembler
                .toModel(activities, activity -> EntityModel.of(activityMapper.toDTO(activity)));
    }

    // Isso vai quebrar em algum momento se não implementar pesquisa paginada
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

    @Deprecated(
            forRemoval = true,
            since = "0.1.0"
    )
    public List<ActivityTsdtDTO> getActivitiesWithTitleStatusTypeAndDeadline() {
        List<Activity> act = (List<Activity>) activityDAO.findAllActivities(DEFAULT_LIMIT);
        if (act.isEmpty()) return Collections.emptyList();
        return act
                .stream()
                .map(activityMapper::toTsdtDTO)
                .toList();
    }

    @Transactional
    public ActivityDTO updateActivity(Long id, ActivityDTO updatedActivity) {
        if (id == null || id <= 0) {
            String msg = messageSource.getMessage(
                    "error.activity.id.invalid",
                    null,
                    LocaleContextHolder.getLocale());
            throw new ConflictException(msg);
        }
        if (!updatedActivity.id().equals(id)) {
            String msg = messageSource.getMessage(
                    "conflict.activity.id.unexpected",
                    null,
                    LocaleContextHolder.getLocale());
            throw new ConflictException(msg);
        }
        Activity activity = activityDAO.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage(
                            "error.activity.not.found",
                            null,
                            LocaleContextHolder.getLocale());
                    return new ResourceNotFoundException(msg);
                });

        activityMapper.updateEntityFromDto(updatedActivity, activity);
        // Esqueci de mencionar que ao buscar uma entidade, ela se torna gerenciada. Portanto, é só alterar os campos com setters.
        // Encaminhei a responsabilidade disso para o MapStruct, mas poderia ser feito aqui também.
        return activityMapper.toDTO(activity);
    }

    public void deleteActivity(Long id) {

        Activity activityToBeDeleted = activityDAO.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage(
                            "error.activity.not.found",
                            null,
                            LocaleContextHolder.getLocale());
                    return new ResourceNotFoundException(msg);
                });

        activityDAO.delete(activityToBeDeleted);
    }

    public ActivityDTO loadActivityData(Long id) {
        Activity activityToBeLoaded = activityDAO.findById(id)
                .orElseThrow(() -> {
                    String msg = messageSource.getMessage(
                            "error.activity.not.found",
                            null,
                            LocaleContextHolder.getLocale());
                    return new ResourceNotFoundException(msg);
                });
        return activityMapper.toDTO(activityToBeLoaded);
    }

    public ActivityDTO completeActivity(Long activityId) {
        Activity activity = activityDAO.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("error.activity.not.found"));

        if (activity.isCompleted()) throw new ConflictException("conflict.activity.completed");

        activity.setCompleted(true);
        activity.setCompletionTimestamp(LocalDateTime.now());

        return activityMapper.toDTO(activity);
    }
}
