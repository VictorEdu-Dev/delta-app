package org.deltacore.delta.domains.auth.dto;

import org.deltacore.delta.domains.auth.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserBasicMapper.class})
public interface UserBasicMapper {
    UserBasicDTO toDTO(User user);
}
