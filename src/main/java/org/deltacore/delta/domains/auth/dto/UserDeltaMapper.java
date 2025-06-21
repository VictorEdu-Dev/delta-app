package org.deltacore.delta.domains.auth.dto;

import org.deltacore.delta.domains.auth.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserDeltaMapper {

    User toEntity(UserDTO userDTO);

    @Named("fullMapping")
    UserDTO toDTO(User user);

    @Named("noLazy")
    @Mapping(target = "tutorings", ignore = true)
    UserDTO toDTONoLazyRelationship(User user);

    void updateEntityFromDTO(UserDTO userDTO, @MappingTarget User user);
}
