package org.deltacore.delta.domains.tutoring.servive;

import org.deltacore.delta.domains.profile.exception.UserNotFoundException;
import org.deltacore.delta.domains.profile.model.Tutor;
import org.deltacore.delta.domains.tutoring.dto.*;
import org.deltacore.delta.domains.tutoring.exception.ConflictException;
import org.deltacore.delta.domains.tutoring.exception.SubjectNotFoundException;
import org.deltacore.delta.domains.tutoring.model.DayTimeEntry;
import org.deltacore.delta.domains.tutoring.model.Tutoring;
import org.deltacore.delta.domains.tutoring.repository.DayTimeEntryDAO;
import org.deltacore.delta.domains.tutoring.repository.TutoringDAO;
import org.deltacore.delta.domains.profile.repository.TutorDAO;
import org.deltacore.delta.domains.tutoring.repository.SubjectDAO;
import org.deltacore.delta.shared.security.AuthenticatedUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class TutoringCommandService {
    private AuthenticatedUserProvider userProvider;
    private final TutorDAO tutorDAO;
    private final SubjectDAO subjectDAO;
    private final TutoringDAO tutoringDAO;
    private final DayTimeEntryDAO dayTimeEntryDAO;
    private final SubjectMapper subjectMapper;
    private final MonitorMapper monitorMapper;
    private final TutoringMapper tutoringMapper;
    private DayTimeEntryMapper dayTimeEntryMapper;


    public TutoringCommandService(TutorDAO tutorDAO,
                                  SubjectDAO subjectDAO,
                                  TutoringDAO tutoringDAO,
                                  DayTimeEntryDAO dayTimeEntryDAO,
                                  TutoringMapper tutoringMapper,
                                  SubjectMapper subjectMapper,
                                  MonitorMapper monitorMapper) {
        this.tutorDAO = tutorDAO;
        this.subjectDAO = subjectDAO;
        this.subjectMapper = subjectMapper;
        this.monitorMapper = monitorMapper;
        this.tutoringDAO = tutoringDAO;
        this.dayTimeEntryDAO = dayTimeEntryDAO;
        this.tutoringMapper = tutoringMapper;
    }

    @Transactional
    public TutoringDTO registerMonitoring(TutoringDTO monitoring) {
        Tutoring tutoringToSave = tutoringMapper.toEntity(monitoring);

        String username = userProvider.currentUsername();
        Optional<Tutor> optionalTutor = tutorDAO.findByUserUsername(username);
        optionalTutor.ifPresent(tutor -> {
            Tutoring t = tutor.getTutoring();
            if (t != null) {
                throw new ConflictException("User already has an active tutoring session");
            }
        });

        tutoringToSave.setMonitor(tutorDAO.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Monitor not found")));
        tutoringToSave.setSubject(subjectDAO.findByCode(monitoring.subject().code())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found")));


        tutoringToSave.setCreatedAt(LocalDateTime.now());
        tutoringToSave.setIsActive(true);

        tutoringToSave = tutoringDAO.save(tutoringToSave);

        if (monitoring.daysOfWeek() != null && !monitoring.daysOfWeek().isEmpty()) {
            Tutoring finalTutoringToSave = tutoringToSave;
            List<DayTimeEntry> entries = monitoring.daysOfWeek().stream()
                    .map(entryDTO -> {
                        DayTimeEntry entry = dayTimeEntryMapper.toEntity(entryDTO);
                        entry.setTutoring(finalTutoringToSave);
                        return entry;
                    }).toList();

            dayTimeEntryDAO.saveAll(entries);
            tutoringToSave.setDaysOfWeek(new HashSet<>(entries));
        }

        TutoringDTO tutoringDTO = tutoringMapper.toDTO(tutoringToSave);

        var maskedUser = tutoringDTO.monitor().userMonitor()
                .toBuilder()
                .passwordHash("*****************")
                .build();

        var updatedMonitor = tutoringDTO.monitor()
                .toBuilder()
                .userMonitor(maskedUser)
                .build();

        return tutoringDTO
                .toBuilder()
                .monitor(updatedMonitor)
                .build();
    }

    @Transactional
    public TutoringDTO updateMonitoring(TutoringDTO updatedDTO) {
        String username = userProvider.currentUsername();
        Tutor tutor = tutorDAO.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Monitor not found"));
        Tutoring existing = tutor.getTutoring();

        tutoringMapper.updateEntityFromDto(updatedDTO, existing);
        return tutoringMapper.toDTO(existing);
    }

    @Transactional
    public void deactivateTutoring() {
        chooseActTutoring(false);
    }

    @Transactional
    public void reactivateTutoring() {
        chooseActTutoring(true);
    }

    private void chooseActTutoring(boolean force) {
        String username = userProvider.currentUsername();
        Tutor tutor = tutorDAO.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Monitor not found"));

        Tutoring existing = tutor.getTutoring();
        existing.setIsActive(force);
    }

    @Autowired @Lazy
    public void setUserProvider(AuthenticatedUserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Autowired @Lazy
    public void setDayTimeEntryMapper(DayTimeEntryMapper dayTimeEntryMapper) {
        this.dayTimeEntryMapper = dayTimeEntryMapper;
    }
}
