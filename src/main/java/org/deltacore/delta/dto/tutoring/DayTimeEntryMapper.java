package org.deltacore.delta.dto.tutoring;

import org.deltacore.delta.model.tutoring.DayTimeEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DayTimeEntryMapper {
    DayTimeEntryDTO toDTO(DayTimeEntry dayTimeEntry);
    DayTimeEntry toEntity(DayTimeEntryDTO dayTimeEntryDTO);
}
