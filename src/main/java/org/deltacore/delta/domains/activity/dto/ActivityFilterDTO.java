package org.deltacore.delta.domains.activity.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record ActivityFilterDTO(ActivityStatus status,

                                ActivityType activityType,

                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                @FutureOrPresent(message = "{activity.deadline.future_or_present}")
                                LocalDate startDate,

                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                @FutureOrPresent(message = "{activity.deadline.future_or_present}")
                                LocalDate endDate) {}
