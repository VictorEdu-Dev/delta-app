package org.deltacore.delta.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ActivityDetailsDTO(
        Long id,
        String title,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String status,
        List<AttachmentDTO> attachments,
        List<ActivityHistoryDTO> history
) {}