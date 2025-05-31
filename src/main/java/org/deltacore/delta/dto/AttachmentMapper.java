package org.deltacore.delta.dto;

import org.deltacore.delta.model.Attachment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    AttachmentDTO toDTO(Attachment attachment);
    Attachment toEntity(AttachmentDTO attachmentDTO);
    List<AttachmentDTO> toDTOList(List<Attachment> attachments);
}
