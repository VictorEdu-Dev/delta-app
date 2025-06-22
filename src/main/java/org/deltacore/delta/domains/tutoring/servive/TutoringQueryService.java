package org.deltacore.delta.domains.tutoring.servive;

import org.deltacore.delta.domains.tutoring.dto.MonitorMapper;
import org.deltacore.delta.domains.tutoring.dto.SubjectMapper;
import org.deltacore.delta.domains.tutoring.dto.MonitorDTO;
import org.deltacore.delta.domains.tutoring.dto.SubjectDTO;
import org.deltacore.delta.domains.tutoring.model.Subject;
import org.deltacore.delta.domains.profile.model.Tutor;
import org.deltacore.delta.domains.profile.repository.TutorDAO;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutoringQueryService {

    private final TutorDAO tutorDAO;
    private final SubjectDAO subjectDAO;
    private final SubjectMapper subjectMapper;
    private final MonitorMapper monitorMapper;

    public TutoringQueryService(TutorDAO tutorDAO,
                                SubjectDAO subjectDAO,
                                SubjectMapper subjectMapper,
                                MonitorMapper monitorMapper) {
        this.tutorDAO = tutorDAO;
        this.subjectDAO = subjectDAO;
        this.subjectMapper = subjectMapper;
        this.monitorMapper = monitorMapper;
    }

    public Optional<SubjectDTO> findSubjectByCode(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        Optional<Subject> subject = subjectDAO.findByCode(code.toUpperCase());
        return subject.map(subjectMapper::toDTO);
    }

    public List<SubjectDTO> findAllSubjects() {
        List<Subject> subjects = (List<Subject>) subjectDAO.findAll();
        return subjects.stream()
                .map(subjectMapper::toDTO)
                .toList();
    }

    public Optional<MonitorDTO> findMonitorByUserUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        Optional<Tutor> monitor = tutorDAO.findByUserUsername(username.toLowerCase());
        return monitor.map(monitorMapper::toDTO);
    }

}
