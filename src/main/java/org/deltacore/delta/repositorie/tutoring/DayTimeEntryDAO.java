package org.deltacore.delta.repositorie.tutoring;

import org.deltacore.delta.model.tutoring.DayTimeEntry;
import org.springframework.data.repository.CrudRepository;

public interface DayTimeEntryDAO extends CrudRepository<DayTimeEntry, Long> {
}
