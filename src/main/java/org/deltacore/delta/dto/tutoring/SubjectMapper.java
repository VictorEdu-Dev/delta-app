package org.deltacore.delta.dto.tutoring;

import org.deltacore.delta.model.tutoring.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectDTO toDTO(Subject subject);

    Subject toEntity(SubjectDTO dto);
}
