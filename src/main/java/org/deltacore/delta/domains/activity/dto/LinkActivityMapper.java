package org.deltacore.delta.domains.activity.dto;

import org.deltacore.delta.domains.activity.model.LinkActivity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LinkActivityMapper {
    ActivityDTO.ActivityRegister.LinkActivityDTO toDTO(LinkActivity linkActivity);
    LinkActivity toEntity(ActivityDTO.ActivityRegister.LinkActivityDTO linkActivityDTO);
}
