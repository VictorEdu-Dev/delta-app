package org.deltacore.delta.domains.profile.dto;

import org.deltacore.delta.domains.profile.model.Tutor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TutorMapper {
    @Mapping(target = "userMonitor", ignore = true)
    @Mapping(target = "tutoring", ignore = true)
    TutorDTO toDTO(Tutor tutor);

    @Mapping(target = "userMonitor", ignore = true)
    @Mapping(target = "tutoring", ignore = true)
    Tutor toEntity(TutorDTO tutorDTO);

    @Mapping(target = "userMonitor", ignore = true)
    @Mapping(target = "tutoring", ignore = true)
    Tutor updateEntityFromDTO(TutorDTO tutorDTO, @MappingTarget Tutor tutor);
}
