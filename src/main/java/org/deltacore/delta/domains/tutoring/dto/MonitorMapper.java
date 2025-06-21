package org.deltacore.delta.domains.tutoring.dto;

import org.deltacore.delta.domains.auth.model.Tutor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorMapper {
    MonitorDTO toDTO(Tutor tutor);
    Tutor toEntity(MonitorDTO monitorDTO);
}
