package org.deltacore.delta.domains.tutoring.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.deltacore.delta.domains.profile.dto.UserDTO;
import org.deltacore.delta.shared.dto.OnCreate;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record TutoringDTO(
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

        @NotNull(message = "{tutoring.is_active.not_null}", groups =  OnCreate.class)
        Boolean isActive,

        LocalDateTime createdAt,

        SubjectDTO subject,

        MonitorDTO monitor,

        List<DayTimeEntryDTO> daysOfWeek,

        List<UserDTO> users
) {
        public record TutoringEssentialDTO(
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

                @NotNull(message = "{tutoring.is_active.not_null}", groups =  OnCreate.class)
                Boolean isActive,

                LocalDateTime createdAt,

                SubjectDTO subject,

                List<DayTimeEntryDTO> daysOfWeek,

                List<UserDTO.UserSimpleDTO> users
        ){}
}
