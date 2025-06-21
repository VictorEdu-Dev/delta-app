package org.deltacore.delta.domains.tutoring.servive;

import org.deltacore.delta.domains.profile.exception.UserNotFound;
import org.deltacore.delta.domains.tutoring.dto.MonitorMapper;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO;
import org.deltacore.delta.domains.tutoring.dto.TutoringMapper;
import org.deltacore.delta.domains.tutoring.dto.SubjectMapper;
import org.deltacore.delta.domains.tutoring.exception.SubjectNotFoundException;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.deltacore.delta.domains.tutoring.repository.DayTimeEntryDAO;
import org.deltacore.delta.domains.tutoring.repository.TutoringDAO;
import org.deltacore.delta.domains.profile.repository.MonitorDAO;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.deltacore.delta.shared.security.AuthenticatedUser;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class TutoringCommandService {
    private AuthenticatedUserProvider userProvider;
    private final MonitorDAO monitorDAO;
    private final SubjectDAO subjectDAO;
    private final TutoringDAO tutoringDAO;
    private final DayTimeEntryDAO dayTimeEntryDAO;
    private final SubjectMapper subjectMapper;
    private final MonitorMapper monitorMapper;
    private final TutoringMapper tutoringMapper;


    public TutoringCommandService(MonitorDAO monitorDAO,
                                  SubjectDAO subjectDAO,
                                  TutoringDAO tutoringDAO,
                                  DayTimeEntryDAO dayTimeEntryDAO,
                                  TutoringMapper tutoringMapper,
                                  SubjectMapper subjectMapper,
                                  MonitorMapper monitorMapper) {
        this.monitorDAO = monitorDAO;
        this.subjectDAO = subjectDAO;
        this.subjectMapper = subjectMapper;
        this.monitorMapper = monitorMapper;
        this.tutoringDAO = tutoringDAO;
        this.dayTimeEntryDAO = dayTimeEntryDAO;
        this.tutoringMapper = tutoringMapper;
    }

    public TutoringDTO registerMonitoring(TutoringDTO monitoring) {
        Tutoring tutoringToSave = tutoringMapper.toEntity(monitoring);

        AuthenticatedUser currentUser = userProvider.current()
                .orElseThrow(() -> new UserNotFound("User not authenticated"));

        tutoringToSave.setTutor(monitorDAO.findByUserUsername(currentUser.username())
                .orElseThrow(() -> new UserNotFound("Monitor not found for user: " + currentUser.username())));
        tutoringToSave.setSubject(subjectDAO.findByCode(monitoring.subject().code())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found for code: " + monitoring.subject().code())));

        tutoringToSave = tutoringDAO.save(tutoringToSave);
        return tutoringMapper.toDTO(tutoringToSave);
    }

    @Autowired @Lazy
    public void setUserProvider(AuthenticatedUserProvider userProvider) {
        this.userProvider = userProvider;
    }
}
