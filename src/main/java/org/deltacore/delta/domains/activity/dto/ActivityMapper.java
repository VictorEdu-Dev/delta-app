package org.deltacore.delta.domains.activity.dto;

import org.deltacore.delta.domains.activity.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    ActivityDTO toDTO(Activity activity);

    Activity toEntity(ActivityDTO.ActivityRegister activityDTO);

    ActivityMiniatureDTO toMiniatureDTO(Activity activity);

    void updateEntityFromDto(ActivityDTO dto, @MappingTarget Activity entity);
}
