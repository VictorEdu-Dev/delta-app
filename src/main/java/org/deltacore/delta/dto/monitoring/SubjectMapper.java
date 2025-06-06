package org.deltacore.delta.dto.monitoring;

import org.deltacore.delta.model.monitoring.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectDTO toDTO(Subject subject);

    Subject toEntity(SubjectDTO dto);
}
