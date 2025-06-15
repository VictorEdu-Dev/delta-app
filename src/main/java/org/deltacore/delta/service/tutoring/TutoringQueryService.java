package org.deltacore.delta.service.tutoring;

import org.deltacore.delta.dto.tutoring.MonitorMapper;
import org.deltacore.delta.dto.tutoring.SubjectMapper;
import org.deltacore.delta.dto.tutoring.MonitorDTO;
import org.deltacore.delta.dto.tutoring.SubjectDTO;
import org.deltacore.delta.model.tutoring.Subject;
import org.deltacore.delta.model.user.Monitor;
import org.deltacore.delta.repositorie.user.MonitorDAO;
import org.deltacore.delta.repositorie.tutoring.SubjectDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TutoringQueryService {

    private final MonitorDAO monitorDAO;
    private final SubjectDAO subjectDAO;
    private final SubjectMapper subjectMapper;
    private final MonitorMapper monitorMapper;

    public TutoringQueryService(MonitorDAO monitorDAO,
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
        Optional<Monitor> monitor = monitorDAO.findByUserUsername(username.toLowerCase());
        return monitor.map(monitorMapper::toDTO);
    }

}
