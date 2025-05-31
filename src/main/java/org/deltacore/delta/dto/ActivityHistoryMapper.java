package org.deltacore.delta.dto;

import org.deltacore.delta.model.ActivityHistory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityHistoryMapper {
    ActivityHistoryDTO toDTO(ActivityHistory activityHistory);
    ActivityHistory toEntity(ActivityHistoryDTO activityHistoryDTO);
    List<ActivityHistoryDTO> toDTOList(List<ActivityHistory> activityHistories);
}