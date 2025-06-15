package org.deltacore.delta.dto.activities;

import org.deltacore.delta.model.Activity;
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
    ActivityTsdtDTO toTsdtDTO(Activity activity);

    void updateEntityFromDto(ActivityDTO dto, @MappingTarget Activity entity);
}
