package org.deltacore.delta.domains.auth.dto;

import org.deltacore.delta.domains.auth.model.RefreshToken;
import org.mapstruct.Mapper;
import static org.deltacore.delta.domains.auth.dto.TokenInfoDTO.RefreshTokenDTO;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {
    RefreshTokenDTO toDTO(RefreshToken entity);
    RefreshToken toEntity(RefreshTokenDTO dto);
}
