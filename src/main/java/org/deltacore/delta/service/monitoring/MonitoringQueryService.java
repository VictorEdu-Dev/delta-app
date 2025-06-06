package org.deltacore.delta.service.monitoring;

import org.deltacore.delta.dto.monitoring.MonitorMapper;
import org.deltacore.delta.dto.monitoring.SubjectMapper;
import org.deltacore.delta.dto.monitoring.MonitorDTO;
import org.deltacore.delta.dto.monitoring.SubjectDTO;
import org.deltacore.delta.model.monitoring.Subject;
import org.deltacore.delta.model.user.Monitor;
import org.deltacore.delta.repositorie.MonitorDAO;
import org.deltacore.delta.repositorie.SubjectDAO;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class MonitoringQueryService {

    private final MonitorDAO monitorDAO;
    private final SubjectDAO subjectDAO;
    private final SubjectMapper subjectMapper;
    private final MonitorMapper monitorMapper;

    public MonitoringQueryService(MonitorDAO monitorDAO,
                                  SubjectDAO subjectDAO,
                                  SubjectMapper subjectMapper,
                                  MonitorMapper monitorMapper) {
        this.monitorDAO = monitorDAO;
        this.subjectDAO = subjectDAO;
        this.subjectMapper = subjectMapper;
        this.monitorMapper = monitorMapper;
    }

    public Optional<SubjectDTO> findSubjectByCode(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        Optional<Subject> subject = subjectDAO.findByCode(code.toUpperCase());
        return subject.map(subjectMapper::toDTO);
    }

    public Optional<MonitorDTO> findMonitorByUserUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        Optional<Monitor> monitor = monitorDAO.findByUserUsername(username);
        return monitor.map(monitorMapper::toDTO);
    }

}
