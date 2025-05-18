package org.deltacore.delta.repositorie;

import org.deltacore.delta.model.Activity;
import org.deltacore.delta.model.ActivityStatus;
import org.deltacore.delta.model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityDAO extends JpaRepository<Activity, UUID> {
    @Query(value = "SELECT * FROM activity WHERE LOWER(TRIM(title)) LIKE LOWER(CONCAT('%', ?1, '%')) LIMIT ?2", nativeQuery = true)
    Iterable<Activity> findActivitiesByTitle(String title, Integer limit);

    @Query(value = "SELECT * FROM activity WHERE LOWER(TRIM(title)) LIKE LOWER(CONCAT('%', ?1, '%')) LIMIT 10", nativeQuery = true)
    Iterable<Activity> findActivitiesByTitle(String title);

    @Query(value = "SELECT * FROM activity WHERE title LIKE %?1% AND activityType = ?2 LIMIT ?3", nativeQuery = true)
    Iterable<Activity> findActivitiesByTitleAndType(String title, ActivityType type, Integer limit);

    @Query(value = "SELECT * FROM activity WHERE title LIKE %?1% AND activityType = ?2 LIMIT 10", nativeQuery = true)
    Iterable<Activity> findActivitiesByTitleAndType(String title, ActivityType type);

    @Query(value = "SELECT * FROM activity WHERE activityType = ?1 LIMIT ?2", nativeQuery = true)
    Iterable<Activity> findActivitiesByActivityType(ActivityType type, Integer limit);

    @Query(value = "SELECT * FROM activity WHERE activityType = ?1", nativeQuery = true)
    Iterable<Activity> findActivitiesByActivityType(ActivityType type);

    @Query(value = "SELECT * FROM activity LIMIT 10", nativeQuery = true)
    Iterable<Activity> findAllActivities();

    @Query(value = "SELECT * FROM activity LIMIT ?1", nativeQuery = true)
    Iterable<Activity> findAllActivities(Integer limit);

    @Query(value = "SELECT * FROM activity", nativeQuery = true)
    Page<Activity> findAllActivitiesOrder(Pageable pageable);

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
}
