package org.deltacore.delta.domains.tutoring.servive;

import org.deltacore.delta.domains.tutoring.dto.MonitorMapper;
import org.deltacore.delta.domains.tutoring.dto.TutoringDTO;
import org.deltacore.delta.domains.tutoring.dto.TutoringMapper;
import org.deltacore.delta.domains.tutoring.dto.SubjectMapper;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.deltacore.delta.domains.tutoring.repository.DayTimeEntryDAO;
import org.deltacore.delta.domains.tutoring.repository.TutoringDAO;
import org.deltacore.delta.domains.auth.repository.MonitorDAO;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.springframework.stereotype.Service;

@Service
public class TutoringCommandService {
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
        tutoringToSave = tutoringDAO.save(tutoringToSave);
        return tutoringMapper.toDTO(tutoringToSave);
    }
}
