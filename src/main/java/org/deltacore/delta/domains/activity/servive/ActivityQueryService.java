package org.deltacore.delta.domains.activity.servive;

import org.deltacore.delta.domains.activity.dto.ActivityFilterDTO;
import org.deltacore.delta.domains.activity.dto.ActivityMiniatureDTO;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.repository.ActivityDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

@Service
public class ActivityQueryService {
    private ActivityDAO activityDAO;
    PagedResourcesAssembler<ActivityMiniatureDTO> pagedResourcesAssembler;

    public PagedModel<EntityModel<ActivityMiniatureDTO>> getActivityMiniature(Pageable pageable, ActivityFilterDTO filters) {
        Page<ActivityMiniatureDTO> activities = activityDAO.findActivitiesMiniature(
                filters.status(),
                filters.activityType(),
                pageable);

        if (activities.isEmpty()) return PagedModel.empty();

        return pagedResourcesAssembler.toModel(activities);
    }

    @Autowired @Lazy
    private void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    @Autowired @Lazy
    private void setPagedResourcesAssembler(PagedResourcesAssembler<ActivityMiniatureDTO> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }
}
