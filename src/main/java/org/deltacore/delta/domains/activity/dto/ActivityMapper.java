package org.deltacore.delta.domains.activity.dto;

import org.deltacore.delta.domains.activity.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    ActivityDTO toDTO(Activity activity);

    Activity toEntity(ActivityDTO activityDTO);

    @Deprecated(
            forRemoval = true,
            since = "0.1.0"
    )
    ActivityMiniatureDTO toTsdtDTO(Activity activity);

    ActivityMiniatureDTO toMiniatureDTO(Activity activity);

    void updateEntityFromDto(ActivityDTO dto, @MappingTarget Activity entity);
}
