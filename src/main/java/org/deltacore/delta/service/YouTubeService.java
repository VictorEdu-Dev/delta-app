package org.deltacore.delta.service;

import org.deltacore.delta.config.YouTubeApiClient;
import org.deltacore.delta.dto.VideoLessonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YouTubeService {

    private final YouTubeApiClient youTubeApiClient;

    @Autowired
    public YouTubeService(YouTubeApiClient youTubeApiClient) {
        this.youTubeApiClient = youTubeApiClient;
    }

    public VideoLessonDTO.VideoItem getVideoDetails(String videoId) {
        VideoLessonDTO response = youTubeApiClient.fetchVideoDetails(videoId);
        String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

        if (response == null || response.items().isEmpty()) {
            return null;
        }
        var original = response.items().getFirst();

        return VideoLessonDTO.VideoItem.builder()
                .id(videoId)
                .videoUrl(videoUrl)
                .snippet(VideoLessonDTO.Snippet.builder()
                        .title(original.snippet().title())
                        .description(original.snippet().description())
                        .build())
                .statistics(original.statistics())
                .contentDetails(original.contentDetails())
                .build();
    }
}