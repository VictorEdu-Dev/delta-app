package org.deltacore.delta.repositorie;

import org.deltacore.delta.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentDAO extends JpaRepository<Attachment, Long> {
    List<Attachment> findByActivityId(Long activityId);
}
