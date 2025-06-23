package org.deltacore.delta.domains.tutoring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TutoringDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @Size(max = 150, message = "{tutoring.description.size}")
        String description,

        @Size(max = 1000, message = "{tutoring.urlThumbnail.size}")
        @URL(message = "{tutoring.urlThumbnail.format}", regexp = "^(https?|ftp)://.*")
        String urlThumbnail,

        @Size(max = 50, message = "{tutoring.location.size}")
        String location,

        @NotNull(message = "{tutoring.vacancies.min}")
        @Min(value = 1, message = "{tutoring.vacancies.min}")
        Integer vacancies,

        @GabeModality(message = "{tutoring.mode.compliant}")
        String mode,

        @NotNull(message = "{tutoring.is_active.not_null}")
        Boolean isActive,

        LocalDateTime createdAt,

        SubjectDTO subject,

        @Schema(hidden = true)
        MonitorDTO monitor,

        List<DayTimeEntryDTO> daysOfWeek,

        @Schema(hidden = true)
        List<UserDTO> users
) {
}
