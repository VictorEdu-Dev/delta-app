package org.deltacore.delta.domains.activity.dto;

import org.deltacore.delta.domains.activity.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {LinkActivityMapper.class, ActivityFilesMapper.class})
public interface ActivityMapper {
    @Mapping(target = "links", ignore = true)
    @Mapping(target = "files", ignore = true)
    ActivityDTO toDTO(Activity activity);

    Activity toEntity(ActivityDTO.ActivityRegister activityDTO);

    ActivityMiniatureDTO toMiniatureDTO(Activity activity);

    void updateEntityFromDto(ActivityDTO dto, @MappingTarget Activity entity);
}
