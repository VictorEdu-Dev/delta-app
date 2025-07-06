package org.deltacore.delta.domains.activity.dto;

import org.deltacore.delta.domains.activity.model.ActivityFiles;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityFilesMapper {
    ActivityFilesDTO toDTO(ActivityFiles entity);

    ActivityFiles toEntity(ActivityFilesDTO dto);
}
