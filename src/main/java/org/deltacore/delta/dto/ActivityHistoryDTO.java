package org.deltacore.delta.dto;

import java.time.LocalDateTime;

public record ActivityHistoryDTO(LocalDateTime timestamp, String user, String action) {
}
