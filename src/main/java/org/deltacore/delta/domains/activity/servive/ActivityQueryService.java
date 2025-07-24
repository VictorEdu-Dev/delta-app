package org.deltacore.delta.domains.activity.servive;

import org.deltacore.delta.domains.activity.dto.ActivityDTO;
import org.deltacore.delta.domains.activity.dto.ActivityFilterDTO;
import org.deltacore.delta.domains.activity.dto.ActivityMapper;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;
import org.deltacore.delta.domains.activity.repository.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityQueryService {
    private ActivityMapper activityMapper;
    private ActivityDAO activityDAO;

    @Transactional
    public Page<ActivityDTO> getFilteredActivities(String search, int page, int size, ActivityFilterDTO filter) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Activity> spec = buildSpecification(search, filter)
                .and(orderByCustomStatus())
                .and(orderByDeadlineAsc());

        return activityDAO.findAll(spec, pageable)
                .map(activityMapper::toDTO);
    }

    private Specification<Activity> buildSpecification(String search, ActivityFilterDTO filter) {
        Specification<Activity> spec = Specification.where(null);

        if (hasText(search)) {
            String pattern = "%" + search.toLowerCase() + "%";
            spec = spec.and(titleOrDescriptionLike(pattern));
        }

        if (filter != null) {
            if (filter.status() != null && !filter.status().trim().isEmpty()) {
                spec = spec.and(statusEquals(ActivityStatus.valueOf(filter.status().toUpperCase())));
            }
            if (filter.activityType() != null && !filter.activityType().trim().isEmpty()) {
                spec = spec.and(activityTypeEquals(ActivityType.valueOf(filter.activityType().toUpperCase())));
            }
        }

        return spec;
    }

    private boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }

    private Specification<Activity> titleOrDescriptionLike(String pattern) {
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("title")), pattern),
                builder.like(builder.lower(root.get("description")), pattern)
        );
    }

    private Specification<Activity> statusEquals(ActivityStatus status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }

    private Specification<Activity> activityTypeEquals(ActivityType type) {
        return (root, query, builder) -> builder.equal(root.get("activityType"), type);
    }

    private Specification<Activity> orderByCustomStatus() {
        return (root, query, builder) -> {
            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
                query.orderBy(builder.asc(
                        builder.selectCase(root.get("status").as(String.class))
                                .when("PENDING", 0)
                                .when("OVERDUE", 1)
                                .when("COMPLETED", 2)
                                .otherwise(3)
                ));
            }
            return builder.conjunction();
        };
    }

    private Specification<Activity> orderByDeadlineAsc() {
        return (root, query, builder) -> {
            if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class)
                query.orderBy(builder.asc(root.get("deadline")));
            return builder.conjunction();
        };
    }

    @Autowired
    private void setActivityMapper(@Lazy ActivityMapper activityMapper){
        this.activityMapper = activityMapper;
    }

    @Autowired
    private void setActivityDAO(@Lazy ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }
}
