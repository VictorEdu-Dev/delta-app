package org.deltacore.delta.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record VideoLessonDTO(List<VideoItem> items) {

    @Builder
    public record VideoItem(
            Long videoId,
            String id,
            String videoUrl,
            Snippet snippet,
            Statistics statistics,
            ContentDetails contentDetails
    ) {}

    @Builder
    public record Snippet(
            String title,
            String description
    ) {}

    @Builder
    public record Statistics(
            String viewCount
    ) {}

    @Builder
    public record ContentDetails(
            String duration
    ) {}
}
