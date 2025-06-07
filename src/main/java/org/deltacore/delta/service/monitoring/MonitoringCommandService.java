package org.deltacore.delta.service.monitoring;

import org.deltacore.delta.dto.monitoring.MonitorMapper;
import org.deltacore.delta.dto.monitoring.SubjectMapper;
import org.deltacore.delta.repositorie.MonitorDAO;
import org.deltacore.delta.repositorie.SubjectDAO;
import org.springframework.stereotype.Service;

@Service
public class MonitoringCommandService {
    private final MonitorDAO monitorDAO;
    private final SubjectDAO subjectDAO;
    private final SubjectMapper subjectMapper;
    private final MonitorMapper monitorMapper;

    public MonitoringCommandService(MonitorDAO monitorDAO,
                                  SubjectDAO subjectDAO,
                                  SubjectMapper subjectMapper,
                                  MonitorMapper monitorMapper) {
        this.monitorDAO = monitorDAO;
        this.subjectDAO = subjectDAO;
        this.subjectMapper = subjectMapper;
        this.monitorMapper = monitorMapper;
    }
}
