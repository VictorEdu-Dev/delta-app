package org.deltacore.delta.service.monitoring;

import org.deltacore.delta.dto.monitoring.MonitorMapper;
import org.deltacore.delta.dto.monitoring.TutoringDTO;
import org.deltacore.delta.dto.monitoring.TutoringMapper;
import org.deltacore.delta.dto.monitoring.SubjectMapper;
import org.deltacore.delta.model.monitoring.Tutoring;
import org.deltacore.delta.repositorie.monitoring.DayTimeEntryDAO;
import org.deltacore.delta.repositorie.monitoring.MonitoringDAO;
import org.deltacore.delta.repositorie.user.MonitorDAO;
import org.deltacore.delta.repositorie.monitoring.SubjectDAO;
import org.springframework.stereotype.Service;

@Service
public class MonitoringCommandService {
    private final MonitorDAO monitorDAO;
    private final SubjectDAO subjectDAO;
    private final MonitoringDAO monitoringDAO;
    private final DayTimeEntryDAO dayTimeEntryDAO;
    private final SubjectMapper subjectMapper;
    private final MonitorMapper monitorMapper;
    private final TutoringMapper tutoringMapper;


    public MonitoringCommandService(MonitorDAO monitorDAO,
                                    SubjectDAO subjectDAO,
                                    MonitoringDAO monitoringDAO,
                                    DayTimeEntryDAO dayTimeEntryDAO,
                                    TutoringMapper tutoringMapper,
                                    SubjectMapper subjectMapper,
                                    MonitorMapper monitorMapper) {
        this.monitorDAO = monitorDAO;
        this.subjectDAO = subjectDAO;
        this.subjectMapper = subjectMapper;
        this.monitorMapper = monitorMapper;
        this.monitoringDAO = monitoringDAO;
        this.dayTimeEntryDAO = dayTimeEntryDAO;
        this.tutoringMapper = tutoringMapper;
    }

    public TutoringDTO registerMonitoring(TutoringDTO monitoring) {
        Tutoring tutoringToSave = tutoringMapper.toEntity(monitoring);
        tutoringToSave = monitoringDAO.save(tutoringToSave);
        return tutoringMapper.toDTO(tutoringToSave);
    }
}
