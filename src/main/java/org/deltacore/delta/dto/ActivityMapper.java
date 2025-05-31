package org.deltacore.delta.dto;

import org.deltacore.delta.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    ActivityDTO toDTO(Activity activity);

    Activity toEntity(ActivityDTO activityDTO);

    ActivityTsdtDTO toTsdtDTO(Activity activity);

    void updateEntityFromDto(ActivityDTO dto, @MappingTarget Activity entity);
}