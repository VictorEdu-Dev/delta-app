package org.deltacore.delta.domains.profile.dto;

import org.deltacore.delta.domains.profile.model.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDTO toDTO(Profile profile);

    Profile toEntity(ProfileDTO profileDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ProfileDTO.ProfileUpdateDTO dto, @MappingTarget Profile profile);
}
