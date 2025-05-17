package org.deltacore.delta.repositorie;

import org.deltacore.delta.model.Activity;
import org.deltacore.delta.model.ActivityType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityDAO extends CrudRepository<Activity, UUID> {
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

    @Query(value = "SELECT * FROM activity ORDER BY deadline ASC LIMIT ?1", nativeQuery = true)
    List<Activity> findAllActivitiesOrderAsc(Integer limit);

    @Query(value = "SELECT * FROM activity ORDER BY deadline DESC LIMIT ?1", nativeQuery = true)
    List<Activity> findAllActivitiesOrderDesc(Integer limit);

    @Query(value = "SELECT title FROM activity WHERE title = ?1", nativeQuery = true)
    Optional<String> findActByTitle(String title);
}
