package org.deltacore.delta.repositorie.monitoring;

import org.deltacore.delta.model.monitoring.DayTimeEntry;
import org.springframework.data.repository.CrudRepository;

public interface DayTimeEntryDAO extends CrudRepository<DayTimeEntry, Long> {
}
