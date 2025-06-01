package org.deltacore.delta.dto.user;

import org.deltacore.delta.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserDeltaMapper {

    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    void updateEntityFromDTO(UserDTO userDTO, @MappingTarget User user);
}
