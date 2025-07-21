package org.deltacore.delta.domains.tutoring.servive;

import org.deltacore.delta.domains.tutoring.dto.*;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO.TutoringEssentialDTO;
import org.deltacore.delta.domains.tutoring.model.Subject;
import org.deltacore.delta.domains.profile.model.Tutor;
import org.deltacore.delta.domains.profile.repository.TutorDAO;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.deltacore.delta.domains.tutoring.repository.TutoringDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TutoringQueryService {

    private final TutorDAO tutorDAO;
    private final SubjectDAO subjectDAO;
    private TutoringDAO tutoringDAO;
    private TutoringMapper tutoringMapper;
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


    @Transactional
    public TutoringEssentialDTO getTutoring(String monitor) {
        Tutoring tutoring = tutoringDAO.findByMonitor_UserMonitor_Username(monitor)
                .orElse(null);
        return tutoringMapper.toEssentialDTO(tutoring);
    }

    public Optional<SubjectDTO> findSubjectByCode(String code) {
        if (code == null || code.isBlank()) return Optional.empty();
        Optional<Subject> subject = subjectDAO.findByCode(code.toUpperCase());
        return subject.map(subjectMapper::toDTO);
    }

    public List<SubjectDTO> findAllSubjects() {
        List<Subject> subjects = subjectDAO.findAllSubjects();
        return subjects.stream()
                .map(subjectMapper::toDTO)
                .toList();
    }

    public Optional<MonitorDTO> findMonitorByUserUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        Optional<Tutor> monitor = tutorDAO.findByUserUsername(username.toLowerCase());
        return monitor.map(monitorMapper::toDTO);
    }

    @Autowired
    public void setTutoringDAO(TutoringDAO tutoringDAO) {
        this.tutoringDAO = tutoringDAO;
    }

    @Autowired
    public void setTutoringMapper(TutoringMapper tutoringMapper) {
        this.tutoringMapper = tutoringMapper;
    }

}
