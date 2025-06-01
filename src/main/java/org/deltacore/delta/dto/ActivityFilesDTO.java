package org.deltacore.delta.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ActivityFilesDTO(

    Long id,
    @NotBlank
    String fileName,

    @NotBlank
    String fileType,

    @NotBlank
    String filePath,

    @Min(value = 1)
    Long size,

    Long activityId
) {
}
