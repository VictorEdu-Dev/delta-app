package org.deltacore.delta.domains.tutoring.dto;

import org.deltacore.delta.domains.profile.dto.UserMapper;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
        SubjectMapper.class,
        MonitorMapper.class,
        DayTimeEntryMapper.class,
        UserMapper.class }
)
public interface TutoringMapper {
    Tutoring toEntity(TutoringDTO tutoringDTO);
    TutoringDTO toDTO(Tutoring tutoring);
}
