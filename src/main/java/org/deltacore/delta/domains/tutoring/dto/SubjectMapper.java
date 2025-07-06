package org.deltacore.delta.domains.tutoring.dto;

import org.deltacore.delta.domains.tutoring.model.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {

    SubjectDTO toDTO(Subject subject);

    Subject toEntity(SubjectDTO dto);
}
