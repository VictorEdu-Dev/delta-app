package org.deltacore.delta.dto;

import org.deltacore.delta.model.ActivityStatus;
import org.deltacore.delta.model.ActivityType;

import java.time.LocalDateTime;

public record ActivityTsdtDTO(String title,
                              String status,
                              String activityType,
                              LocalDateTime deadline) {}
