package org.deltacore.delta.domains.activity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
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

    ActivityDTO activity
) {
}
