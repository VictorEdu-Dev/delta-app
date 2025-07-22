package org.deltacore.delta.domains.activity.repository;

import org.deltacore.delta.domains.activity.dto.ActivityDTO.ActivityRegister.LinkActivityDTO;
import org.deltacore.delta.domains.activity.dto.ActivityMiniatureDTO;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ActivityDAO extends CrudRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {
    @Query("SELECT a FROM Activity a " +
            "WHERE (:status IS NULL OR a.status = :status) " +
            "AND (:activityType IS NULL OR a.activityType = :activityType) " +
            "AND a.deadline BETWEEN :startDate AND :endDate")
    Page<Activity> findActivitiesByFilters(@Param("status") ActivityStatus status,
                                           @Param("activityType") ActivityType activityType,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);


    @Query(value = "SELECT title FROM activity WHERE title = ?1", nativeQuery = true)
    Optional<String> findActByTitle(String title);

    @Query(value = "SELECT new org.deltacore.delta.domains.activity.dto.ActivityMiniatureDTO(u.id, u.title, u.status, u.activityType, u.maxScore, u.imageUrl, u.difficultyLevel, u.deadline) FROM Activity u " +
                    "WHERE (:status IS NULL OR u.status = :status) " +
                    "AND (:activityType IS NULL OR u.activityType = :activityType) ")
    Page<ActivityMiniatureDTO> findActivitiesMiniature(@Param("status") ActivityStatus status,
                                                       @Param("activityType") ActivityType activityType,
                                                       Pageable pageable);

    @Query(value = """
        SELECT DISTINCT l.link, l.description
        FROM activity a
        INNER JOIN activity_links l ON a.id = l.activity_id
        WHERE a.id = :activityId
    """, nativeQuery = true)
    List<LinkActivityDTO> findLinksByActivityId(@Param("activityId") Long activityId);

}
