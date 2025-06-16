package org.deltacore.delta.domains.tutoring.dto;

import org.deltacore.delta.domains.tutoring.model.DayTimeEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DayTimeEntryMapper {
    DayTimeEntryDTO toDTO(DayTimeEntry dayTimeEntry);
    DayTimeEntry toEntity(DayTimeEntryDTO dayTimeEntryDTO);
}
