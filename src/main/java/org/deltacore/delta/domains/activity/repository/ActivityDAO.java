package org.deltacore.delta.domains.activity.repository;

import org.deltacore.delta.domains.activity.dto.ActivityMiniatureDTO;
import org.deltacore.delta.domains.activity.model.Activity;
import org.deltacore.delta.domains.activity.model.ActivityStatus;
import org.deltacore.delta.domains.activity.model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import java.time.LocalDateTime;
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
    @Override
    @NonNull
    @EntityGraph(attributePaths = {"files", "links"})
    Page<Activity> findAll(Specification<Activity> spec, @NonNull Pageable pageable);

}
