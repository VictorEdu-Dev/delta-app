package org.deltacore.delta.domains.tutoring.dto;

import org.deltacore.delta.domains.auth.model.Monitor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorMapper {
    MonitorDTO toDTO(Monitor monitor);
    Monitor toEntity(MonitorDTO monitorDTO);
}
