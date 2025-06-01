package org.deltacore.delta.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.deltacore.delta.dto.ActivityDTO;
import org.deltacore.delta.dto.ActivityMapper;
import org.deltacore.delta.exception.ConflictException;
import org.deltacore.delta.model.Activity;
import org.deltacore.delta.model.ActivityType;
import org.deltacore.delta.repositorie.ActivityDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//class ActivitiesSectionServiceTest {
//
//    @Mock
//    private ActivityDAO activityDAO;
//
//    @Mock
//    private ActivityMapper activityMapper;
//
//    @InjectMocks
//    private ActivitiesSectionService activitiesSectionService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//    @Test
//    void shouldMarkActivityAsCompleted() {
//        Long id = 1L;
//        Activity activity = new Activity();
//        activity.setId(id);
//        activity.setCompleted(false);
//
//        when(activityDAO.findById(id)).thenReturn(Optional.of(activity));
//        when(activityDAO.save(any())).thenReturn(activity);
//        when(activityMapper.toDTO(any())).thenReturn(mock(ActivityDTO.class));
//
//        ActivityDTO result = activitiesSectionService.completeActivity(id);
//
//        assertNotNull(result);
//        assertTrue(activity.isCompleted());
//        assertNotNull(activity.getCompletionTimestamp());
//
//        verify(activityDAO).save(activity);
//    }
//
//    @Test
//    void shouldThrowConflictExceptionIfActivityAlreadyCompleted() {
//        Long id = 1L;
//        Activity activity = new Activity();
//        activity.setId(id);
//        activity.setCompleted(true);
//
//        when(activityDAO.findById(id)).thenReturn(Optional.of(activity));
//
//        assertThrows(ConflictException.class, () -> activitiesSectionService.completeActivity(id));
//        verify(activityDAO, never()).save(any());
//    }
//}
//    @Test
//    void shouldSaveActivity() {
//        ActivityDTO dto = new ActivityDTO(
//                10L,
//                "Title",
//                "",
//                ActivityType.CHALLENGE,
//                "http://img.com/img.png",
//                1,
//                BigDecimal.TEN
//        );
//
//        System.out.println("Validating DTO: " + dto);
//
//        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
//            Validator validator = factory.getValidator();
//
//            Set<ConstraintViolation<ActivityDTO>> violations = validator.validate(dto);
//            if (!violations.isEmpty()) {
//                for (ConstraintViolation<ActivityDTO> violation : violations) {
//                    System.err.println(violation.getMessage());
//                }
//                return;
//            }
//        }
//
//
//        Activity entity = new Activity();
//        Mockito.when(activityMapper.toEntity(dto)).thenReturn(entity);
//        when(activityMapper.toDTO(any(Activity.class))).thenAnswer(invocation -> {
//            Activity a = invocation.getArgument(0);
//            return new ActivityDTO(
//                    a.getId(),
//                    a.getTitle(),
//                    a.getDescription(),
//                    a.getActivityType(),
//                    a.getImageUrl(),
//                    a.getRecommendedLevel(),
//                    a.getMaxScore()
//            );
//        });
//
//        activitiesSectionService.saveActivity(dto);
//
//        Mockito.verify(activityDAO).save(entity);
//    }
//
//    @Test
//    void testGetLimitedActivitiesWithEmptySearch() {
//        List<Activity> mockActivities = new ArrayList<>();
//        mockActivities.add(Activity.builder()
//                                .id(9L)
//                                .title("Test Activity 00")
//                                .description("Description 00")
//                                .activityType(ActivityType.CHALLENGE)
//                                .imageUrl("image_00.jpg")
//                                .recommendedLevel(1)
//                                .maxScore(BigDecimal.valueOf(100))
//                                .videoUrl(null)
//                                .subject(null)
//                                .build());
//
//        when(activityDAO.findAllActivities(20)).thenReturn(mockActivities);
//        when(activityMapper.toDTO(any(Activity.class))).thenAnswer(invocation -> {
//            Activity a = invocation.getArgument(0);
//            return new ActivityDTO(
//                    a.getId(),
//                    a.getTitle(),
//                    a.getDescription(),
//                    a.getActivityType(),
//                    a.getImageUrl(),
//                    a.getRecommendedLevel(),
//                    a.getMaxScore()
//            );
//        });
//
//        List<ActivityDTO> result = activitiesSectionService.getLimitedActivities("");
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void testGetLimitedActivitiesWithNullSearch() {
//        List<Activity> mockActivities = new ArrayList<>();
//        mockActivities.add(Activity.builder()
//                                .id(10L)
//                                .title("Test Activity 01")
//                                .description("Description 01")
//                                .activityType(ActivityType.CHALLENGE)
//                                .imageUrl("image_01.jpg")
//                                .recommendedLevel(3)
//                                .maxScore(BigDecimal.valueOf(100))
//                                .videoUrl(null)
//                                .subject(null)
//                                .build());
//
//        when(activityDAO.findAllActivities(20)).thenReturn(mockActivities);
//        when(activityMapper.toDTO(any(Activity.class))).thenAnswer(invocation -> {
//            Activity a = invocation.getArgument(0);
//            return new ActivityDTO(
//                    a.getId(),
//                    a.getTitle(),
//                    a.getDescription(),
//                    a.getActivityType(),
//                    a.getImageUrl(),
//                    a.getRecommendedLevel(),
//                    a.getMaxScore()
//            );
//        });
//
//        List<ActivityDTO> result = activitiesSectionService.getLimitedActivities(null);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }

//    @Test
//    void testSearchDetailedWithMultipleWords() {
//        List<Activity> mockActivities1 = new ArrayList<>();
//        mockActivities1.add(Activity.builder()
//                                .id(1L)
//                                .title("Test Activity 02")
//                                .description("Description 02")
//                                .activityType(ActivityType.CHALLENGE)
//                                .imageUrl("image_02.jpg")
//                                .recommendedLevel(1)
//                                .maxScore(BigDecimal.valueOf(100))
//                                .videoUrl(null)
//                                .subject(null)
//                                .build());
//        mockActivities1.add(Activity.builder()
//                                .id(2L)
//                                .title("Another Test Activity")
//                                .description("Description 03")
//                                .activityType(ActivityType.CHALLENGE)
//                                .imageUrl("image_03.jpg")
//                                .recommendedLevel(2)
//                                .maxScore(BigDecimal.valueOf(150))
//                                .videoUrl(null)
//                                .subject(null)
//                                .build());
//        mockActivities1.add(Activity.builder()
//                                .id(3L)
//                                .title("Test Activity 04")
//                                .description("Description 04")
//                                .activityType(ActivityType.CHALLENGE)
//                                .imageUrl("image_04.jpg")
//                                .recommendedLevel(3)
//                                .maxScore(BigDecimal.valueOf(200))
//                                .videoUrl(null)
//                                .subject(null)
//                                .build());
//
//        List<Activity> mockActivities2 = new ArrayList<>();
//        mockActivities2.add(Activity.builder()
//                                .id(4L)
//                                .title("Test Activity 05")
//                                .description("Description 05")
//                                .activityType(ActivityType.CHALLENGE)
//                                .imageUrl("image_05.jpg")
//                                .recommendedLevel(1)
//                                .maxScore(BigDecimal.valueOf(100))
//                                .videoUrl(null)
//                                .subject(null)
//                                .build());
//        mockActivities2.add(Activity.builder()
//                                .id(5L)
//                                .title("Another Activity 06")
//                                .description("Description 06")
//                                .activityType(ActivityType.CHALLENGE)
//                                .imageUrl("image_06.jpg")
//                                .recommendedLevel(2)
//                                .maxScore(BigDecimal.valueOf(150))
//                                .videoUrl(null)
//                                .subject(null)
//                                .build());
//
//        when(activityDAO.findActivitiesByTitle("Test")).thenReturn(mockActivities1);
//        when(activityDAO.findActivitiesByTitle("Activity")).thenReturn(mockActivities2);
//        when(activityMapper.toDTO(any(Activity.class))).thenAnswer(invocation -> {
//            Activity a = invocation.getArgument(0);
//            return new ActivityDTO(
//                    a.getId(),
//                    a.getTitle(),
//                    a.getDescription(),
//                    a.getActivityType(),
//                    a.getImageUrl(),
//                    a.getRecommendedLevel(),
//                    a.getMaxScore()
//            );
//        });
//
//        List<ActivityDTO> result = activitiesSectionService.getLimitedActivities("Test Activity");
//
//        assertNotNull(result);
//        assertEquals(5, result.size());
//        assertTrue(result.stream().anyMatch(activity -> activity.title().contains("Test Activity")));
//        assertTrue(result.stream().anyMatch(activity -> activity.title().contains("Another Test Activity")));
//    }
//
//    @Test
//    void testSearchDetailedWithNoResults() {
//        when(activityDAO.findActivitiesByTitle("NonExistent")).thenReturn(Collections.emptyList());
//        when(activityMapper.toDTO(any(Activity.class))).thenAnswer(invocation -> {
//            Activity a = invocation.getArgument(0);
//            return new ActivityDTO(
//                    a.getId(),
//                    a.getTitle(),
//                    a.getDescription(),
//                    a.getActivityType(),
//                    a.getImageUrl(),
//                    a.getRecommendedLevel(),
//                    a.getMaxScore()
//            );
//        });
//
//        List<ActivityDTO> result = activitiesSectionService.getLimitedActivities("NonExistent");
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }


