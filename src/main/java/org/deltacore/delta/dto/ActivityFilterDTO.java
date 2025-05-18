package org.deltacore.delta.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import org.deltacore.delta.model.ActivityStatus;
import org.deltacore.delta.model.ActivityType;
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
