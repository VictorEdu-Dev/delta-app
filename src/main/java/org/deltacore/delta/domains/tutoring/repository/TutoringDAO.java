package org.deltacore.delta.domains.tutoring.repository;

import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.springframework.data.repository.CrudRepository;

public interface TutoringDAO extends CrudRepository<Tutoring, Long> {
}
