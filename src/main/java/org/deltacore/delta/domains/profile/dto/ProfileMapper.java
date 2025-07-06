package org.deltacore.delta.domains.profile.dto;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDTO toDto(ProfileDTO profile);

    ProfileDTO toEntity(ProfileDTO profileDto);
}
