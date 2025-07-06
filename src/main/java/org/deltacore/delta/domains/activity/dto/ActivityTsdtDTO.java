package org.deltacore.delta.domains.activity.dto;

import java.time.LocalDateTime;

public record ActivityTsdtDTO(String title,
                              String status,
                              String activityType,
                              LocalDateTime deadline) {}
