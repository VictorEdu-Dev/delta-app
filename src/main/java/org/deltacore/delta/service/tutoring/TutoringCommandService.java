package org.deltacore.delta.service.tutoring;

import org.deltacore.delta.dto.tutoring.MonitorMapper;
import org.deltacore.delta.dto.tutoring.TutoringDTO;
import org.deltacore.delta.dto.tutoring.TutoringMapper;
import org.deltacore.delta.dto.tutoring.SubjectMapper;
import org.deltacore.delta.model.tutoring.Tutoring;
import org.deltacore.delta.repositorie.tutoring.DayTimeEntryDAO;
import org.deltacore.delta.repositorie.tutoring.TutoringDAO;
import org.deltacore.delta.repositorie.user.MonitorDAO;
import org.deltacore.delta.repositorie.tutoring.SubjectDAO;
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
