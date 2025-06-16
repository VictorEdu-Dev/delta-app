package org.deltacore.delta.domains.auth.dto;

import org.deltacore.delta.domains.auth.model.Blacklist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BlacklistMapper {
    Blacklist toEntity(BlacklistDTO dto);
    BlacklistDTO toDTO(Blacklist entity);
}
