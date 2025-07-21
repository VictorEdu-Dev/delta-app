package org.deltacore.delta.domains.tutoring.dto;

import org.deltacore.delta.domains.profile.dto.UserMapper;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

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

    TutoringDTO.TutoringEssentialDTO toEssentialDTO(Tutoring tutoring);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TutoringDTO dto, @MappingTarget Tutoring tutoring);
}
