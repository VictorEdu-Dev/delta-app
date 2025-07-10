package org.deltacore.delta.domains.profile.repository;

import org.deltacore.delta.domains.profile.model.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileDAO extends CrudRepository<Profile, Long> {
}
