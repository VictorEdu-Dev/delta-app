package org.deltacore.delta.dto.monitoring;

import org.deltacore.delta.model.monitoring.DayTimeEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DayTimeEntryMapper {
    DayTimeEntryDTO toDTO(DayTimeEntry dayTimeEntry);
    DayTimeEntry toEntity(DayTimeEntryDTO dayTimeEntryDTO);
}
