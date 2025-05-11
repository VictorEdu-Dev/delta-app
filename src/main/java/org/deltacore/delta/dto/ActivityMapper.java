package org.deltacore.delta.dto;

import org.deltacore.delta.model.Activity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    ActivityDTO toDTO(Activity activity);

    Activity toEntity(ActivityDTO activityDTO);
}
