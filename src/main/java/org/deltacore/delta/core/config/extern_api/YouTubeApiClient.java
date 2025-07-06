package org.deltacore.delta.core.config.extern_api;

import org.deltacore.delta.domains.activity.dto.VideoLessonDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class YouTubeApiClient {
    private static final String YOUTUBE_API_BASE_URL = "https://www.googleapis.com/youtube/v3/videos";

    @Value("${api.key.yt.v3}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public VideoLessonDTO fetchVideoDetails(String videoId) {
        String url = buildVideoDetailsUrl(videoId);
        return restTemplate.getForObject(url, VideoLessonDTO.class);
    }

    private String buildVideoDetailsUrl(String videoId) {
        return String.format(
                YOUTUBE_API_BASE_URL + "?part=snippet,contentDetails,statistics&id=%s&key=%s",
                videoId, apiKey
        );
    }
}

