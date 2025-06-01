package org.deltacore.delta.repositorie;

import org.deltacore.delta.model.ActivityFiles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ActivityFilesDAO extends CrudRepository<ActivityFiles, Long> {
    @Query("SELECT af FROM ActivityFiles af WHERE af.fileName = ?1")
    Optional<ActivityFiles> findByFileName(String fileName);
}
