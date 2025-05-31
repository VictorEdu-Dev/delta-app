package org.deltacore.delta.repositorie;

import org.deltacore.delta.model.ActivityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityHistoryDAO extends JpaRepository<ActivityHistory, Long> {
    List<ActivityHistory> findByActivityId(Long activityId);
}
