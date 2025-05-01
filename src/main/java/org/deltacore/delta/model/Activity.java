package org.deltacore.delta.model;

import java.util.List;
import java.util.UUID;

public class Activity {
    private UUID id;
    private String title;
    private String description;
    private ActivityType activityType;
    private String imageUrl;
    private Integer recommendedLevel;
    private Float maxScore;
    private List<VideoLesson> videoUrl;
    private String videoDuration;
    private String videoThumbnailUrl;
    private String videoLessonId;

    private static enum ActivityType {
        QUIZ,
        EXERCISE,
        CHALLENGE
    }
}
