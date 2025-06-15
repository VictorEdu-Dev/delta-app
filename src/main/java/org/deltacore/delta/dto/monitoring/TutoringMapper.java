package org.deltacore.delta.dto.monitoring;

import org.deltacore.delta.dto.user.UserMapper;
import org.deltacore.delta.model.monitoring.Tutoring;
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
