package org.deltacore.delta.dto.activities;

import org.deltacore.delta.model.ActivityFiles;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActivityFilesMapper {
    ActivityFilesDTO toDTO(ActivityFiles entity);

    ActivityFiles toEntity(ActivityFilesDTO dto);
}
