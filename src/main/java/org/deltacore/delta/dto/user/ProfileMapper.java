package org.deltacore.delta.dto.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileDTO toDto(ProfileDTO profile);

    ProfileDTO toEntity(ProfileDTO profileDto);
}
