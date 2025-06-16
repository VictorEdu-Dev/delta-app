package org.deltacore.delta.domains.tutoring.repository;

import org.deltacore.delta.domains.tutoring.model.DayTimeEntry;
import org.springframework.data.repository.CrudRepository;

public interface DayTimeEntryDAO extends CrudRepository<DayTimeEntry, Long> {
}
