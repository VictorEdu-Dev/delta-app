package org.deltacore.delta.dto.activities;

import java.time.LocalDateTime;

public record ActivityTsdtDTO(String title,
                              String status,
                              String activityType,
                              LocalDateTime deadline) {}
