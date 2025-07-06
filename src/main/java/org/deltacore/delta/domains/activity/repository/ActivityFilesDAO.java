package org.deltacore.delta.domains.activity.repository;

import org.deltacore.delta.domains.activity.model.ActivityFiles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ActivityFilesDAO extends CrudRepository<ActivityFiles, Long> {
    @Query(value = "SELECT af.* FROM activity_files af JOIN activity act ON af.activity_id = act.id WHERE af.file_name LIKE %:templateName% AND act.id = :activicyId", nativeQuery = true)
    Optional<ActivityFiles> findByTemplate(@Param("templateName") String templateName,
                                           @Param("activicyId") Long id);
}
