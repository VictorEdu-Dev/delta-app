package org.deltacore.delta.dto.tutoring;

import org.deltacore.delta.model.user.Monitor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorMapper {
    MonitorDTO toDTO(Monitor monitor);
    Monitor toEntity(MonitorDTO monitorDTO);
}
